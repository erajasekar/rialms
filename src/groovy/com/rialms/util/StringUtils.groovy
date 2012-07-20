package com.rialms.util

/**
 * Created with IntelliJ IDEA.
 * User: relango
 * Date: 7/20/12
 * Time: 12:48 AM
 * To change this template use File | Settings | File Templates.
 */
class StringUtils {

    public static String convertCamelCaseToWords(String input){
        StringBuilder result = new StringBuilder();

        input.each { c ->
            if (c in 'A'..'Z') result.append(' ' + c.toLowerCase());
            else result.append(c)
        }

        return result;
    }
}
