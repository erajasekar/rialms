package com.rialms.util
import org.lesscss.LessCompiler

/**
 * Created by IntelliJ IDEA.
 * User: relango
 * Date: 5/17/12
 * Time: 6:04 PM
 * To change this template use File | Settings | File Templates.
 */
class Less2Css {
    public static void main(String []args){
        LessCompiler lessCompiler = new LessCompiler();
        File src = new File(args[0]);
        File target = new File(args[1]);
        lessCompiler.compile(src,target);
    }
}
