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
        }finally{
            input.delete();
        }

        return contentPackage
    }

}
