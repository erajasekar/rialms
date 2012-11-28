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

    public static File getUniqueFile(File dir , String fileName){
        File file = new File(dir,fileName);
        boolean isDir = file.isDirectory();
        Integer dot         = fileName.lastIndexOf(".")
        String namePart    = (dot > 0 && !isDir) ? fileName[0..dot-1] : fileName
        String extension   = (dot > 0 && !isDir) ? fileName[dot+1..fileName.length()-1] : ""
        int i = 1;
        while (file.exists()) {
            String newName = "${namePart}-${i}"
            if (extension){
                newName = newName + ".${extension}";
            }
            file = new File(dir,newName)
            i++
        }
        return file;
    }
}
