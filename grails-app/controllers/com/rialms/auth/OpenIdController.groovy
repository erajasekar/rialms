import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.codehaus.groovy.grails.plugins.springsecurity.openid.OpenIdAuthenticationFailureHandler as OIAFH

import org.springframework.security.web.savedrequest.DefaultSavedRequest

import com.rialms.auth.User

import groovy.transform.ToString
import org.codehaus.groovy.grails.plugins.springsecurity.ui.RegistrationCode

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.WebAttributes
import org.springframework.security.authentication.AccountExpiredException
import org.springframework.security.authentication.CredentialsExpiredException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.LockedException
import sun.reflect.generics.scope.ConstructorScope
import com.rialms.consts.Constants
import com.rialms.util.AuthService
import grails.plugins.springsecurity.Secured
import org.springframework.security.web.savedrequest.SavedRequest

/**
 * Manages associating OpenIDs with application users, both by creating a new local user
 * associated with an OpenID and also by associating a new OpenID to an existing account.
 */
class OpenIdController {

    private static final String OPEN_ID_USER_PASSWORD = "notused";

    /** Dependency injection for daoAuthenticationProvider. */
    def daoAuthenticationProvider

    /** Dependency injection for OpenIDAuthenticationFilter. */
    def openIDAuthenticationFilter

    /** Dependency injection for the springSecurityService. */
    def springSecurityService

    def emailService;

    def grailsApplication;

    def authService;

    static defaultAction = 'auth'

    /**
     * Shows the login page. The user has the choice between using an OpenID and a email
     * and password for a local account. If an OpenID authentication is successful but there
     * is no corresponding local account, they'll be redirected to createAccount to create
     * a new account, or click through to linkAccount to associate the OpenID with an
     * existing local account.
     */
    def auth = {
        def config = SpringSecurityUtils.securityConfig;
        if (springSecurityService.isLoggedIn()) {

            return redirectToTarget();
        }
        OpenIdRegisterCommand command = new OpenIdRegisterCommand();
        copyFromAttributeExchange(command);

        //Redirect to previously accessed url, if user tries to access a page without logging in and user is redirected to login page.
        SavedRequest savedRequest = (SavedRequest) session.getAttribute(WebAttributes.SAVED_REQUEST);
        if (savedRequest != null) {
            params.targetUrl = savedRequest.getRedirectUrl() - "$request.scheme://$request.serverName:$request.serverPort$request.contextPath";
        }
        return getModel(command, false);
    }

    private Map getModel(OpenIdRegisterCommand command, boolean isSignUp) {
        def config = SpringSecurityUtils.securityConfig;

        return [openIdPostUrl: "${request.contextPath}$openIDAuthenticationFilter.filterProcessesUrl",
                daoPostUrl: "${request.contextPath}${config.apf.filterProcessesUrl}",
                persistentRememberMe: config.rememberMe.persistent,
                rememberMeParameter: config.rememberMe.parameter,
                command: command,
                isSignUp: isSignUp,
                openIdIdentifierValue: grailsApplication.config.rialms.googleOpenIdIdentifier,
                openidIdentifier: config.openid.claimedIdentityFieldName]

    }
    /**
     * Initially we're redirected here after a UserNotFoundException with a valid OpenID
     * authentication. This action is specified by the openid.registration.createAccountUri
     * attribute.
     */
    def createAccount = { OpenIdRegisterCommand command ->

        String openId = session[OIAFH.LAST_OPENID_USERNAME]
        List exchangeAttrsList = session[OIAFH.LAST_OPENID_ATTRIBUTES];

        if (!(openId || exchangeAttrsList)) {
            flash.error = g.message(code: 'openId.notfound.error');
            return [command: command]
        }
        Map exchangeAttrs = exchangeAttrsList.collectEntries {[it.name, it.values]}
        String email = exchangeAttrs.email.get(0);
        String password = OPEN_ID_USER_PASSWORD;
        String name = exchangeAttrs.firstName.get(0) + ' ' + exchangeAttrs.lastName.get(0)

        User user = User.findByEmail(email);

        if (!user) {
            log.info("No existing account found for ${email}, creating new user");
            flash.message = message(code: 'register.complete.message');
            if (!authService.createNewAccount(email, password, name)) {
                log.error("Error in creating new account");
                render(view: 'auth', model: [command: command, isSignUp: true]);
                return;
            }
        } else {
            log.info("Existing user account found for ${email}, adding open Id to user");
            authService.addOpenIdToUser(user, openId);
        }
        authenticateAndRedirect email
    }

    def signUpAccount = {OpenIdRegisterCommand command ->
        def config = SpringSecurityUtils.securityConfig
        String openIdPostUrl = "${request.contextPath}$openIDAuthenticationFilter.filterProcessesUrl";
        String daoPostUrl = "${request.contextPath}${config.apf.filterProcessesUrl}";
        Map model = getModel(command, true);
        if (command.hasErrors()) {
            render(view: 'auth', model: model);
            return;
        }
        if (!authService.createNewAccount(command.email, command.password, command.displayName, true)) {
            log.error("Error in creating new account");
            render(view: 'auth', model: model);
            return;
        }
        //Account creation success
        else {
            emailService.sendVerifyRegistration(command.email, command.displayName);
            model.emailSent = true;
            render(view: 'auth', model: model);
        }
    }

    def forgotPassword = { ForgotPasswordCommand command ->

        if (!request.post) {
            // show the form
            return
        }
        log.info("forgot password params ${params}");
        if (command.hasErrors()) {
            render(view: 'forgotPassword', model: [command: command]);
            return;
        }

        emailService.sendForgotPassword(params.email);

        [emailSent: true]
    }

    def resetPassword = { ResetPasswordCommand command ->

        String token = params.t

        def registrationCode = token ? RegistrationCode.findByToken(token) : null
        if (!registrationCode) {
            flash.error = message(code: 'spring.security.ui.resetPassword.badCode')
            return [command: command]
        }

        if (!request.post) {
            return [token: token, command: new ResetPasswordCommand()]
        }

        command.email = registrationCode.username
        command.validate()

        if (command.hasErrors()) {
            return [token: token, command: command]
        }

        if (authService.resetPassword(registrationCode, command.password)) {
            springSecurityService.reauthenticate command.email;
            flash.message = message(code: 'resetPassword.success.message')
            def conf = SpringSecurityUtils.securityConfig
            params[Constants.targetUrl] = conf.ui.register.postResetUrl;
            redirect(action: 'authSuccess');
        } else {
            flash.error = message(code: 'resetPassword.error.message')
            return [token: token, command: command]
        }
    }

    def verifyRegistration = {

        def conf = SpringSecurityUtils.securityConfig
        String defaultTargetUrl = conf.successHandler.defaultTargetUrl

        String token = params.t

        def registrationCode = token ? RegistrationCode.findByToken(token) : null
        if (!registrationCode) {
            flash.error = message(code: 'spring.security.ui.register.badCode')
            redirect uri: defaultTargetUrl
            return
        }

        User user = authService.activateUser(registrationCode);

        springSecurityService.reauthenticate user.email

        flash.message = message(code: 'register.complete.message')
        params[Constants.targetUrl] = conf.ui.register.postRegisterUrl;
        redirect(action: 'authSuccess');
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def editProfile = { EditProfileCommand command ->

        User user = session[Constants.currentUser];

        if (!request.post) {            // show the form

            command = new EditProfileCommand(email: user.email, displayName: user.displayName);
            log.debug("DEBUG editProfileCommand errors ${command.errors} ");
            return [command: command]
        }
        command.validate()
        if (command.hasErrors()) {
            return [command: command]
        }

        if (authService.updateUser(command.email, command.displayName, command.newPassword)) {
            springSecurityService.reauthenticate user.email;
            flash.message = message(code: 'editProfile.success.message')
            redirect(action: 'authSuccess');
        } else {
            flash.error = message(code: 'editProfile.error.message')
            return [command: command];
        }
    }

    /**
     * Callback after a failed login. Redirects to the auth page with a warning message.
     */
    def authfail = {

        def username = session[UsernamePasswordAuthenticationFilter.SPRING_SECURITY_LAST_USERNAME_KEY]
        String msg = ''
        def exception = session[WebAttributes.AUTHENTICATION_EXCEPTION]
        if (exception) {
            if (exception instanceof AccountExpiredException) {
                msg = g.message(code: "springSecurity.errors.login.expired")
            }
            else if (exception instanceof CredentialsExpiredException) {
                msg = g.message(code: "springSecurity.errors.login.passwordExpired")
            }
            else if (exception instanceof DisabledException) {
                msg = g.message(code: "springSecurity.errors.login.disabled")
            }
            else if (exception instanceof LockedException) {
                msg = g.message(code: "springSecurity.errors.login.locked")
            }
            else {
                msg = g.message(code: "springSecurity.errors.login.fail")
            }
        }

        flash.message = msg
        redirect action: 'auth', params: params
    }

    def authSuccess = {
        postLoginSuccess();
        flash.message = flash.message;
        flash.error = flash.error;
        redirectToTarget();
    }

    private void redirectToTarget() {
        log.info("DEBUG redirectToTarget params ${params}");
        def conf = SpringSecurityUtils.securityConfig
        String targetUrl = params[Constants.targetUrl]
        if (targetUrl == null || targetUrl.equals('null')) {
            targetUrl = conf.successHandler.defaultTargetUrl;
        }
        log.debug("DEBUG redirecting to ${targetUrl}");
        redirect(uri: targetUrl);
    }

    private void postLoginSuccess() {
        User currentUser = authService.postLoginSuccess();
        session[Constants.currentUser] = currentUser;
        log.info("${currentUser.displayName} successfully authenticated");
    }
    /**
     * Authenticate the user for real now that the account exists/is linked and redirect
     * to the originally-requested uri if there's a SavedRequest.
     *
     * @param username the user's login name
     */
    protected void authenticateAndRedirect(String username) {
        session.removeAttribute OIAFH.LAST_OPENID_USERNAME
        session.removeAttribute OIAFH.LAST_OPENID_ATTRIBUTES

        springSecurityService.reauthenticate username

        postLoginSuccess()

        def config = SpringSecurityUtils.securityConfig

        def savedRequest = session[DefaultSavedRequest.SPRING_SECURITY_SAVED_REQUEST_KEY]
        if (savedRequest && !config.successHandler.alwaysUseDefault) {
            redirect url: savedRequest.redirectUrl
        }
        else {
            redirect uri: config.successHandler.defaultTargetUrl
        }
    }

    /**
     * For the initial form display, copy any registered AX values into the command.
     * @param command the command
     */
    protected void copyFromAttributeExchange(OpenIdRegisterCommand command) {
        List attributes = session[OIAFH.LAST_OPENID_ATTRIBUTES] ?: []
        for (attribute in attributes) {
            String name = attribute.name
            if (command.hasProperty(name)) {
                command."$name" = attribute.values[0]
            }
        }
    }

    static final passwordValidator = { String password, command ->
        if (password) {
            if (!checkPasswordMinLength(password)) {
                return 'command.password.error.minSize'
            }
            if (!checkPasswordMaxLength(password)) {
                return 'command.password.error.maxSize'
            }
            if (!checkPasswordRegex(password)) {
                return 'command.password.error.strength'
            }
        }
    }

    static final currentPasswordValidator = { String password, command ->
        if (password && command.email) {
            String currentPassword = User.findByEmail(command.email).password;
            if (!currentPassword.equals(command.authService.encodePassword(password))) {
                return 'editProfileCommand.currentPassword.error.invalid';
            }
        }
    }

    static boolean checkPasswordMinLength(String password) {
        def conf = SpringSecurityUtils.securityConfig

        int minLength = conf.ui.password.minLength instanceof Number ? conf.ui.password.minLength : 8

        password && password.length() >= minLength
    }

    static boolean checkPasswordMaxLength(String password) {
        def conf = SpringSecurityUtils.securityConfig

        int maxLength = conf.ui.password.maxLength instanceof Number ? conf.ui.password.maxLength : 64

        password && password.length() <= maxLength
    }

    static boolean checkPasswordRegex(String password) {
        def conf = SpringSecurityUtils.securityConfig

        String passValidationRegex = conf.ui.password.validationRegex ?:
            '^.*(?=.*\\d)(?=.*[a-zA-Z]).*$'

        password && password.matches(passValidationRegex)
    }
}

@ToString(includeFields = true)
class OpenIdRegisterCommand {

    String email = ""
    String displayName = ""
    String password = ""

    static constraints = {

        email blank: false, email: true, validator: { String email, command ->
            User.withNewSession { session ->
                if (email && User.countByEmail(email)) {
                    return 'openIdRegisterCommand.email.error.unique'
                }
            }
        }

        displayName blank: false

        password blank: false, validator: OpenIdController.passwordValidator;
    }
    //TODO P3: Temporary hack to workaround bug in jquery-validation-ui plugin
    public boolean isAttached() {
        return false;
    }
}

/**
 * This is class is only used to add validations and translate them to jquery validation
 * using jquery-validation-ui plugin.
 */
class LoginCommand {

    String j_username = ""
    String j_password = ""

    static constraints = {
        j_username blank: false, email: true
        j_password blank: false
    }
    //TODO P3: Temporary hack to workaround bug in jquery-validation-ui plugin
    public boolean isAttached() {
        return false;
    }

}
//Used only for validation
class ForgotPasswordCommand {

    String email = ""
    static constraints = {

        email blank: false, email: true, validator: { String email, command ->
            User.withNewSession { session ->
                if (email && !User.countByEmail(email)) {
                    return 'forgotPasswordCommand.user.notFound'
                }
            }
        }
    }
    //TODO P3: Temporary hack to workaround bug in jquery-validation-ui plugin
    public boolean isAttached() {
        return false;
    }
}

class ResetPasswordCommand {
    String email;
    String password

    static constraints = {
        email blank: false, email: true
        password blank: false, validator: OpenIdController.passwordValidator
    }

    //TODO P3: Temporary hack to workaround bug in jquery-validation-ui plugin
    public boolean isAttached() {
        return false;
    }
}

@ToString(includeFields = true)
class EditProfileCommand {

    AuthService authService;
    String email = ""
    String displayName = ""
    String newPassword = ""
    String currentPassword = ""

    static constraints = {

        displayName blank: false
        newPassword blank: true, nullable: true, validator: OpenIdController.passwordValidator;
        currentPassword blank: false, validator: OpenIdController.currentPasswordValidator;
    }
    //TODO P3: Temporary hack to workaround bug in jquery-validation-ui plugin
    public boolean isAttached() {
        return false;
    }
}
