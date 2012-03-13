package com.rialms.util

import com.sun.org.apache.xpath.internal.XPathAPI;
import groovy.xml.DOMBuilder
import groovy.xml.dom.DOMCategory

/**
 * Created by IntelliJ IDEA.
 * User: relango
 * Date: 3/13/12
 * Time: 5:31 PM
 * To change this template use File | Settings | File Templates.
 */
class SampleParsing {

    public static void main(String[] args) {
        File input = new File("D:\\Raja\\projects\\rialms\\web-app\\content\\exercise\\perimeter1.xml");
        println "here";

        /*def doc = DOMBuilder.parse(new FileReader(input))
        def root = doc.documentElement
        use(DOMCategory) {
            XPathAPI.selectNodeList(root, '//itemBody').each { n->
                println "node ${n}";
            }
        } */


    }

}
