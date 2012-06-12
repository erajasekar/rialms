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

    public static final String headerTitle = "${Consts.assessmentHeader}.${Consts.title}";
    public static final String headerButton = "${Consts.assessmentHeader}.${Consts.buttons}";

    public static String getHeaderButton(String identifier) {
        return "${headerButton}.${identifier}";
    }

    public static getProperty(String object,String property){
        return "${object}.${property}";
    }

    public static getProperty(String object,String... properties){
        StringBuilder result = new StringBuilder(object);
        int length = properties.size();
        for (int i = 0; i < length - 1; i++) {
            result.append(properties[i]).append('.');
        }
        result.append(properties[length - 1])
    }

    public static String getTemplateVar(String variable) {
        return "{{${variable}}}";
    }

    public static class PropertyConstructor{
        String object;
        public PropertyConstructor(String object){
            this.object = object;
        }
        public String getProperty(String property){
            return getProperty(this.object,property);
        }

        public String getPropertyValue(String property){
            return getTemplateVar(getProperty(property));
        }
        public String getProperties(String... properties){
            return getProperty(this.object,properties);
        }

        public String getPropertiesValue(String... properties){
            return getTemplateVar(getProperty(properties));
        }
    }
}
