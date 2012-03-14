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
        for (def child in node.parent.children()) {
            println("child " +child);
            if ((child instanceof groovy.util.slurpersupport.Node)) {
                result = child;
               println "1=> " + result;
                break;
            }
        }

        println "2 => " + result;
        return result
    /*    code from net
        def children = node.children()
    def result;
    for (child in children) {
        if (!(child instanceof groovy.util.slurpersupport.Node)) {
            result.add(child)
        }
    }
    return result       */
    }

}
