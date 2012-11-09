package com.rialms.util

import org.springframework.beans.factory.InitializingBean
import org.codehaus.groovy.grails.plugins.springsecurity.ui.RegistrationCode
import com.rialms.auth.User
import com.rialms.auth.UserRole
import com.rialms.auth.Role
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

class AuthService implements InitializingBean {

    def grailsApplication;
    def messageSource;
    def conf;
    def securityConfig;
    def springSecurityService;

    void afterPropertiesSet() {
        conf = grailsApplication.config;
        securityConfig = SpringSecurityUtils.securityConfig;
    }

    public User activateUser(RegistrationCode registrationCode) {
        User user = null;
        RegistrationCode.withTransaction { status ->
            user = User.findByEmail(registrationCode.username)
            if (user) {
                user.accountLocked = false
                user.save(flush: true)
                addUserToRole(user);
                registrationCode.delete();
                log.info("Successfully activated user ${registrationCode.username}");
            }
        }
        return user;
    }

    public boolean createNewAccount(String email, String password, String name,boolean accountLocked) {

        boolean created = User.withTransaction { status ->
            def config = SpringSecurityUtils.securityConfig
            password = encodePassword(password)

            def user = createUser(email, password, name, accountLocked);

            if (!user) {
                return false
            }
            if (!accountLocked) {
                for (roleName in config.openid.registration.roleNames) {
                    UserRole.create user, Role.findByAuthority(roleName)
                }
            }
            return true
        }

        log.info("createdNewAccount for ${email} with result : ${created}");
        return created;
    }

    public User createUser(String email, String password, String name, boolean accountLocked){

        User user = new User(email: email, password: password, displayName: name, enabled: true, accountLocked: accountLocked);
        if (!user.save(flush: true)) {
            user.errors.allErrors.each {log.error(messageSource.getMessage(it, null))}
            return null;
        }
        return user;
    }

    public User createUser(String email, String password, String name){
        return createUser(email, password, name, false);
    }

    public boolean createNewAccount(String email, String password, String name) {
        return createNewAccount(email, password, name, false);
    }

    public void addOpenIdToUser(User user, String openId) {
        User.withTransaction { status ->
            user.addToOpenIds(url: openId)
            if (!user.validate()) {
                status.setRollbackOnly()
            }
        }
    }

    private void addUserToRole(User user) {
        for (roleName in conf.rialms.security.defaultRoleNames) {
            UserRole.create user, Role.findByAuthority(roleName)
        }
    }

    public void resetPassword(RegistrationCode registrationCode, String password) {
        RegistrationCode.withTransaction { status ->
            def user = User.findByEmail(registrationCode.username)
            user.password = encodePassword(password);
            user.save()
            registrationCode.delete();
            log.info("Password reset for user ${user.displayName}");
        }

    }

    public User postLoginSuccess(){
        User user = springSecurityService.currentUser;
        user.lastLogggedIn = new Date();

        user.loginCount++;
        user.save();
        if (user.hasErrors()){
            log.error("Error in postLoginSuccess update");
            user.errors.allErrors.each {log.error(messageSource.getMessage(it, null))}
        }
        return user;
    }

    private String encodePassword(String password) {
        def encode = securityConfig.openid.encodePassword
        if (!(encode instanceof Boolean) || (encode instanceof Boolean && encode)) {
            password = springSecurityService.encodePassword(password)
        }
        password
    }

}
