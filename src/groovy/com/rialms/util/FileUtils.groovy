package com.rialms.util

/**
 * Created with IntelliJ IDEA.
 * User: relango
 * Date: 10/17/12
 * Time: 12:14 AM
 * To change this template use File | Settings | File Templates.
 */
class FileUtils {
    public static String getBaseName(String fileName){
        return fileName.lastIndexOf('.').with {it != -1 ? fileName[0..<it] : fileName}
    }
}
