package com.rialms.assessment

import org.qtitools.util.ContentPackage
import org.springframework.beans.factory.InitializingBean
import com.rialms.util.FileUtils

class PackageService implements InitializingBean {
    static transactional = false;
    static scope = "singleton"
    String contentPath;
    def grailsApplication;


    public ContentPackage unpackContent(File input) {
        File dest = new File(input.getParentFile(),FileUtils.getBaseName(input.name));

        ContentPackage contentPackage = new ContentPackage(input)
        //TODO p2 add validation
        try {
            contentPackage.unpack(dest, true)
        } catch (Exception e) {
          //TODO P0 HANDLE EXCEPTION
        }

        return contentPackage
    }
    //TODO p2 remove if not used
    void afterPropertiesSet() {
        contentPath = grailsApplication.config.rialms.contentPath;
    }
}
