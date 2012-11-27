package com.rialms.assessment

import org.springframework.beans.factory.InitializingBean
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import com.rialms.auth.User

class UploadService implements InitializingBean {

    def grailsApplication;
    def springSecurityService;
    String contentPath;
    File userContentDir = null;
    static scope = "singleton"

    void afterPropertiesSet() {
        contentPath = grailsApplication.config.rialms.contentPath;
    }

    public String getUserContentDir(){
        if (!userContentDir){
            String userName = springSecurityService.currentUser.displayName;
            userContentDir  = new File(grailsApplication.parentContext.getResource(contentPath).getFile(),userName);
            if (!userContentDir.exists()){
                userContentDir.mkdir();
            }
        }
        log.info("DEBUG userContentDir = ${userContentDir}");
        return userContentDir;
    }
}
