package com.rialms.assessment

import org.qtitools.util.ContentPackage
import org.springframework.beans.factory.InitializingBean
import com.rialms.util.FileUtils
import org.qtitools.util.ContentPackageException

class PackageService {
    static transactional = false;
    static scope = "singleton"


    public ContentPackage unpackContent(File input) {
        File dest = FileUtils.getUniqueFile(input.getParentFile(),FileUtils.getBaseName(input.name));

        ContentPackage contentPackage = new ContentPackage(input)
        try {
            contentPackage.unpack(dest, false);
        } catch (ContentPackageException e) {
          log.error("Exception in unpacking content", e);
           //TODO P1: Find better way to handle this exception
          throw e;
        }finally{
            input.delete();
        }

        return contentPackage
    }

}
