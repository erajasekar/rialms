import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.codehaus.groovy.grails.plugins.springsecurity.openid.OpenIdAuthenticationFailureHandler as OIAFH

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.savedrequest.DefaultSavedRequest

import com.rialms.auth.User
import com.rialms.auth.Role
import com.rialms.auth.UserRole
import groovy.transform.ToString
import org.codehaus.groovy.grails.plugins.springsecurity.ui.RegistrationCode
import groovy.text.SimpleTemplateEngine
import org.codehaus.groovy.grails.plugins.springsecurity.NullSaltSource
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.WebAttributes
import org.springframework.security.authentication.AccountExpiredException
import org.springframework.security.authentication.CredentialsExpiredException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.LockedException
import grails.converters.JSON

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
            redirect uri: config.successHandler.defaultTargetUrl
            return
        }
        OpenIdRegisterCommand command = new OpenIdRegisterCommand();
        copyFromAttributeExchange(command);
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
            if (!authService.createNewAccount(email, password, name)) {
                log.error("Error in creating new account");
                render(view: 'auth', model: [command: command, isSignUp: true]);
                return;
            }
        } else {
            log.info("Existing user account found for ${email}, adding open Id to user");
            authService.addOpenIdToUser(user, openId);
        }

        flash.message = message(code: 'register.complete.message');
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
        if (!authService.createNewAccount(command.email, command.password, command.name, true)) {
            log.error("Error in creating new account");
            render(view: 'auth', model: model);
            return;
        }
        //Account creation success
        else {
            emailService.sendVerifyRegistration(command.email, command.name);
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

        authService.resetPassword(registrationCode, command.password);
        springSecurityService.reauthenticate command.email

        flash.message = message(code: 'resetPassword.success.message')

        def conf = SpringSecurityUtils.securityConfig
        String postResetUrl = conf.ui.register.postResetUrl ?: conf.successHandler.defaultTargetUrl
        redirect uri: postResetUrl
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
        redirect uri: conf.ui.register.postRegisterUrl ?: defaultTargetUrl

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
        if (password && password.length() >= 8 && password.length() <= 64 &&
                (!password.matches('^.*\\p{Alpha}.*$') ||
                        !password.matches('^.*\\p{Digit}.*$'))) {
            return 'command.password.error.strength'
        }
    }
}

@ToString(includeFields = true)
class OpenIdRegisterCommand {

    String email = ""
    String name = ""
    String password = ""

    static constraints = {

        email blank: false, email: true, validator: { String email, command ->
            User.withNewSession { session ->
                if (email && User.countByEmail(email)) {
                    return 'openIdRegisterCommand.email.error.unique'
                }
            }
        }

        name blank: false

        password blank: false, minSize: 8, maxSize: 64, validator: OpenIdController.passwordValidator;
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
        password blank: false, minSize: 8, maxSize: 64, validator: OpenIdController.passwordValidator
    }

    //TODO P3: Temporary hack to workaround bug in jquery-validation-ui plugin
    public boolean isAttached() {
        return false;
    }
}
