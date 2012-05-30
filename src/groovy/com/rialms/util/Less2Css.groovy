package com.rialms.util
import org.lesscss.LessCompiler
import groovy.util.logging.Log4j

/**
 * Created by IntelliJ IDEA.
 * User: relango
 * Date: 5/17/12
 * Time: 6:04 PM
 * To change this template use File | Settings | File Templates.
 */
@Log4j
class Less2Css {

    private static final String LESS_SRC_DIR = "src/less";
    private static final String CSS_OUTPUT_DIR = "web-app/css";

    public static void compile(String src, String target){
        LessCompiler lessCompiler = new LessCompiler();
        File srcFile = new File(src);
        File targetFile = new File(target);
        log.info("Compiling ${srcFile.absoluteFile} to ${targetFile.absoluteFile}");
        lessCompiler.compile(srcFile,targetFile);
    }
    
    public static void run(){
       // compile("${LESS_SRC_DIR}/rialms/bootstrapmakker.less","${CSS_OUTPUT_DIR}/bootstrap.css");
        compile("${LESS_SRC_DIR}/rialms/rialms.less","${CSS_OUTPUT_DIR}/rialms.css");
    }
}
