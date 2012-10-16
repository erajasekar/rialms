package com.rialms.assessment

import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.qtitools.util.ContentPackage
import org.springframework.beans.factory.InitializingBean;

class PackageService implements InitializingBean {
    static transactional = false;
    static scope = "session"
    String contentPath;
    def grailsApplication;


    ContentPackage handleZip(InputStream input, File dest) {
//        File dest = ApplicationHolder.application.parentContext.getResource(contentPath + File.separator + 'packages').getFile()
        ContentPackage contentPackage = new ContentPackage(input)

        try {
            contentPackage.unpack(dest, true)
        } catch (Exception e) {
          //TODO P0 HANDLE EXCEPTION
        }

        return contentPackage
    }

    void afterPropertiesSet() {
        contentPath = grailsApplication.config.rialms.contentPath;
    }
}
