import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.codehaus.groovy.grails.plugins.springsecurity.openid.OpenIdAuthenticationFailureHandler as OIAFH

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.savedrequest.DefaultSavedRequest

import com.rialms.auth.User
import com.rialms.auth.Role
import com.rialms.auth.UserRole
import groovy.transform.ToString

/**
 * Manages associating OpenIDs with application users, both by creating a new local user
 * associated with an OpenID and also by associating a new OpenID to an existing account.
 */
class OpenIdController {

	/** Dependency injection for daoAuthenticationProvider. */
	def daoAuthenticationProvider

	/** Dependency injection for OpenIDAuthenticationFilter. */
	def openIDAuthenticationFilter

	/** Dependency injection for the springSecurityService. */
	def springSecurityService

    def grailsApplication;

    static defaultAction = 'auth'

    //TODO P2: Clean up this controller
	/**
	 * Shows the login page. The user has the choice between using an OpenID and a email
	 * and password for a local account. If an OpenID authentication is successful but there
	 * is no corresponding local account, they'll be redirected to createAccount to create
	 * a new account, or click through to linkAccount to associate the OpenID with an
	 * existing local account.
	 */
	def auth = {

		def config = SpringSecurityUtils.securityConfig

		if (springSecurityService.isLoggedIn()) {
			redirect uri: config.successHandler.defaultTargetUrl
			return
		}

		[openIdPostUrl: "${request.contextPath}$openIDAuthenticationFilter.filterProcessesUrl",
		 daoPostUrl:    "${request.contextPath}${config.apf.filterProcessesUrl}",
		 persistentRememberMe: config.rememberMe.persistent,
		 rememberMeParameter: config.rememberMe.parameter,
         command: new OpenIdRegisterCommand(),
         isSignUp:false,
         openIdIdentifierValue : grailsApplication.config.rialms.googleOpenIdIdentifier,
		 openidIdentifier: config.openid.claimedIdentityFieldName]
	}

	/**
	 * Initially we're redirected here after a UserNotFoundException with a valid OpenID
	 * authentication. This action is specified by the openid.registration.createAccountUri
	 * attribute.
	 * <p/>
	 * The GSP displays the OpenID that was received by the external provider and keeps it
	 * in the session rather than passing it between submits so the user has no opportunity
	 * to change it.
	 */
	def createAccount = { OpenIdRegisterCommand command ->

		String openId = session[OIAFH.LAST_OPENID_USERNAME]
        List exchangeAttrsList =  session[OIAFH.LAST_OPENID_ATTRIBUTES];

		if (!(openId || exchangeAttrsList)) {
			flash.error = 'Sorry, an OpenID was not found'
			return [command: command]
		}
        Map exchangeAttrs = exchangeAttrsList.collectEntries {[it.name, it.values]}
        String email = exchangeAttrs.email.get(0);
        String password = 'notused';
        String name = exchangeAttrs.firstName.get(0) + ' ' +  exchangeAttrs.lastName.get(0)

		if (!createNewAccount(email, password, name, openId)) {
            log.error("Error in creating new account");
            render (view: 'auth' ,model: [command: command, isSignUp: true]);
            return ;
		}

		authenticateAndRedirect email
	}

    def signUpAccount = {OpenIdRegisterCommand command ->
        def config = SpringSecurityUtils.securityConfig
        String openIdPostUrl = "${request.contextPath}$openIDAuthenticationFilter.filterProcessesUrl";
        String daoPostUrl = "${request.contextPath}${config.apf.filterProcessesUrl}";
        if (command.hasErrors()) {
            render (view: 'auth' ,
                    model: [command: command,
                            openIdPostUrl: "${openIdPostUrl}",
                            daoPostUrl: "${daoPostUrl}",
                            openIdIdentifierValue : grailsApplication.config.rialms.googleOpenIdIdentifier,
                            openidIdentifier: config.openid.claimedIdentityFieldName,
                            isSignUp: true]
            );
            return;
        }
        if (!createNewAccount(command.email, command.password, command.name, null)) {
            log.error("Error in creating new account");
            render (view: 'auth' , model: [command: command,
                    openIdPostUrl: "${openIdPostUrl}",
                    daoPostUrl: "${daoPostUrl}",
                    openIdIdentifierValue : grailsApplication.config.rialms.googleOpenIdIdentifier,
                    openidIdentifier: config.openid.claimedIdentityFieldName,
                    isSignUp: true]
            );
            return ;
        }
        else{
            authenticateAndRedirect command.email;
        }
    }

    /**
	 * The registration page has a link to this action so an existing user who successfully
	 * authenticated with an OpenID can associate it with their account for future logins.
	 */
	def linkAccount = { OpenIdLinkAccountCommand command ->

		String openId = session[OIAFH.LAST_OPENID_USERNAME]
		if (!openId) {
			flash.error = 'Sorry, an OpenID was not found'
			return [command: command]
		}

		if (!request.post) {
			// show the form
			command.clearErrors()
			return [command: command, openId: openId]
		}

		if (command.hasErrors()) {
			return [command: command, openId: openId]
		}

		try {
			registerAccountOpenId command.username, command.password, openId
		}
		catch (AuthenticationException e) {
			flash.error = 'Sorry, no user was found with that email and password'
			return [command: command, openId: openId]
		}

		authenticateAndRedirect command.username
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
	 * Create the user instance and grant any roles that are specified in the config
	 * for new users.
	 * @param username  the email
	 * @param password  the password
	 * @param openId  the associated OpenID
	 * @return  true if successful
	 */
	protected boolean createNewAccount(String email, String password, String name, String openId) {
		boolean created = User.withTransaction { status ->
			def config = SpringSecurityUtils.securityConfig

			password = encodePassword(password)
			def user = new User(email: email, password: password, name: name, enabled: true)

            if (openId){
                user.addToOpenIds(url: openId)
            }


			if (!user.save()) {
				return false
			}

			for (roleName in config.openid.registration.roleNames) {
				UserRole.create user, Role.findByAuthority(roleName)
			}
			return true
		}
		return created;
	}

	protected String encodePassword(String password) {
		def config = SpringSecurityUtils.securityConfig
		def encode = config.openid.encodePassword
		if (!(encode instanceof Boolean) || (encode instanceof Boolean && encode)) {
			password = springSecurityService.encodePassword(password)
		}
		password
	}

	/**
	 * Associates an OpenID with an existing account. Needs the user's password to ensure
	 * that the user owns that account, and authenticates to verify before linking.
	 * @param username  the email
	 * @param password  the password
	 * @param openId  the associated OpenID
	 */
	protected void registerAccountOpenId(String username, String password, String openId) {
		// check that the user exists, password is valid, etc. - doesn't actually log in or log out,
		// just checks that user exists, password is valid, account not locked, etc.
		daoAuthenticationProvider.authenticate(
				new UsernamePasswordAuthenticationToken(username, password))

		User.withTransaction { status ->
			def user = User.findByEmail(username)
			user.addToOpenIds(url: openId)
			if (!user.validate()) {
				status.setRollbackOnly()
			}
		}
	}

	/**
	 * For the initial form display, copy any registered AX values into the command.
	 * @param command  the command
	 */
	protected void copyFromAttributeExchange(OpenIdRegisterCommand command) {
		List attributes = session[OIAFH.LAST_OPENID_ATTRIBUTES] ?: []
		for (attribute in attributes) {
			// TODO document
			String name = attribute.name
			if (command.hasProperty(name)) {
				command."$name" = attribute.values[0]
			}
		}
	}

}

@ToString(includeFields=true)
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

		password blank: false, minSize: 8, maxSize: 64, validator: {String password, command ->

            if (password && password.length() >= 8 && password.length() <= 64 &&
                    (!password.matches('^.*\\p{Alpha}.*$') ||
                            !password.matches('^.*\\p{Digit}.*$'))) {
                return 'openIdRegisterCommand.password.error.strength'
            }
        }
	}
    //TODO P3: Temporary hack to workaround bug in jquery-validation-ui plugin
    public boolean isAttached(){
        return false;
    }
}

/**
 * This is class is only used to add validations and translate them to jquery validation
 * using jquery-validation-ui plugin.
 */
class LoginCommand{

    String j_username = ""
    String j_password = ""

    static constraints = {
        j_username blank: false, email: true
        j_password blank: false
    }
    //TODO P3: Temporary hack to workaround bug in jquery-validation-ui plugin
    public boolean isAttached(){
        return false;
    }

}
class OpenIdLinkAccountCommand {

	String username = ""
	String password = ""

	static constraints = {
		username blank: false
		password blank: false
	}
}
