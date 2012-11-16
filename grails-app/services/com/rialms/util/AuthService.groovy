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

    public boolean resetPassword(RegistrationCode registrationCode, String password) {
        boolean result;
        RegistrationCode.withTransaction { status ->
            result = updateUser(registrationCode.username,null,password);
            registrationCode.delete();
            log.info("Password reset for user ${registrationCode.username}");
        }
        return result;
    }

    public boolean updateUser(String email, String name, String password){
        User.withTransaction {
            User user = User.findByEmail(email);
            if (name){
                user.displayName = name;
            }
            if (password){
                user.password = password;
            }
            if (!user.save()) {
                log.error("Error in updating user with email ${email}");
                user.errors.allErrors.each {log.error(messageSource.getMessage(it, null))}
                return false;
            }
            log.info("Successfully updated user with email ${email}");
        }
        return true;
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

    public String encodePassword(String password) {
        springSecurityService.encodePassword(password);
    }

}
