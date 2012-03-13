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
        println "n1=>" + node.text();
        println "c1=>" + node.getClass();

        def result;
        for (def child in node.children) {
            println("child " +child);
            if ((child instanceof java.lang.String)) {
                result = child;
               println "1=> " + result;
                break;
            }
        }

        println "2 => " + result;
        return result
    /**        code from net
        def children = parent.getAt(0).children
    def result = [] as List
    for (child in children) {
        if (!(child instanceof groovy.util.slurpersupport.Node)) {
            result.add(child)
        }
    }
    return result*/
    }

}
