package com.rialms.angular

import com.rialms.consts.Constants as Consts;

/**
 * Created by IntelliJ IDEA.
 * User: relango
 * Date: 6/4/12
 * Time: 11:24 PM
 * To change this template use File | Settings | File Templates.
 */
class JsObjectUtil {

    public static final String headerId = "${Consts.assessmentHeader}.${Consts.id}";
    public static final String headerTitle = "${Consts.assessmentHeader}.${Consts.title}";
    public static final String headerButton = "${Consts.assessmentHeader}.${Consts.buttons}";

    public static String getHeaderButton(String identifier){
         return "${headerButton}.${identifier}";
    }

    public static String getTemplateVar(String variable){
        return "{{${variable}}}";
    }
}
