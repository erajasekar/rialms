package com.rialms.util

import groovy.util.slurpersupport.GPathResult

/**
 * Created by IntelliJ IDEA.
 * User: Rajasekar Elango
 * Date: 3/12/12
 * Time: 6:17 PM
 * To change this template use File | Settings | File Templates.
 */
class XmlUtils {

    public static String getNodeText(def node) {
        def children = node.children
        def result;
        for (child in children) {
            if ((child instanceof java.lang.String)) {
                result = child;
                break;
            }
        }
        return result
    }

}
