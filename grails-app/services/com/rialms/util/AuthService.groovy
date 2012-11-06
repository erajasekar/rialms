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

    public boolean createNewAccount(String email, String password, String name, String openId, boolean accountLocked) {
        boolean created = User.withTransaction { status ->
            def config = SpringSecurityUtils.securityConfig

            password = encodePassword(password)
            def user = new User(email: email, password: password, name: name, enabled: true, accountLocked: accountLocked);

            if (openId) {
                user.addToOpenIds(url: openId)
            }

            if (!user.save()) {
                user.errors.allErrors.each {log.error(messageSource.getMessage(it, null))}
                return false
            }

            if (!accountLocked) {
                for (roleName in config.openid.registration.roleNames) {
                    UserRole.create user, Role.findByAuthority(roleName)
                }
            }
            return true
        }
        return created;
    }

    public boolean createNewAccount(String email, String password, String name, String openId) {
        return createNewAccount(email, password, name, openId, false);
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
            registrationCode.delete()
        }
    }

    private String encodePassword(String password) {
        def encode = securityConfig.openid.encodePassword
        if (!(encode instanceof Boolean) || (encode instanceof Boolean && encode)) {
            password = springSecurityService.encodePassword(password)
        }
        password
    }

}
