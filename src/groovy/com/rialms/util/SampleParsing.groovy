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

        /*def doc = DOMBuilder.parse(new FileReader(input))
        def root = doc.documentElement
        use(DOMCategory) {
            XPathAPI.selectNodeList(root, '//itemBody').each { n->
                println "node ${n}";
            }
        } */

        def xmlRoot = new XmlSlurper().parse(input);

        def itemBody = xmlRoot.itemBody;

        StringBuilder sb = new StringBuilder();
        processItemBody(itemBody,sb);
        println sb;

       // itemBody.children().each { n ->
         //   if (n.name().equals("p")) {
           //     expandP(n);
                //println n.text()
                //  println "----------"
                //  n.children().each { c ->
              //  println "size ${ n.getClass()}"
             //   def children = n.getAt(0).children
             //   def result = [] as List
             //   for (child in children) {
                    /* if (!(child instanceof groovy.util.slurpersupport.Node)) {
                        result.add(child)
                    }*/
             //       println "${child.getClass()} => ${child}";

             //   }
             ////   println "---------------"
                //println "result ${result}"
                //   }
            //}
        //}
    }

    public static void expandP(def n) {
       /* println "${n.getClass()}"
        if (n instanceof groovy.util.slurpersupport.Node){
            println "${n.attributes()}"
        }*/
        def children;
        if (n instanceof groovy.util.slurpersupport.NodeChild) {
            children =  n.getAt(0).children;
        }else if (n instanceof  groovy.util.slurpersupport.Node)  {
            children = n.children();
        }

            for (child in children) {
                if (child instanceof groovy.util.slurpersupport.Node) {
                    if (child.name().equals("p")) {
                        expandP(child);
                    } else {
                        println child.name();
                    }

                } else if (child instanceof String) {
                    println child;
                }
            }


    }

    public static processItemBody(n,sb){
        def children;
      //  println "${n.getClass()} ==> ${n.name()}"
        if (n.name().equalsIgnoreCase("p") && n instanceof groovy.util.slurpersupport.NodeChild) {
            children =  n.getAt(0).children;
        }else{//} if (n instanceof  groovy.util.slurpersupport.Node)  {
            //println "here ${n.children().each{}}";

            children = n.children();

        }

        for (child in children) {
          //  println "${child.getClass()}}"
               /* if (child instanceof groovy.util.slurpersupport.Node) {
                    if (child.name().equals("p")) {
                        sb.append("<p>\n");
                        processItemBody(child,sb);
                        sb.append( "</p>\n"  )
                    } else {
                        println "<${child.name()}>\n";
                    }

                } else if (child instanceof String) {
                    sb.append( child + "\n");
                } */

                if (child instanceof String) {
                    sb.append( child + "\n");
                }else{
                    if (child.name().equals("p")) {
                        sb.append("<p>\n");
                        processItemBody(child,sb);
                        sb.append( "</p>\n"  )
                    } else {
                       Map attributes = child.attributes();
                       sb.append( "<${child.name()}> attr ${attributes?.responseIdentifier}\n");
                    }
                }
            }
    }

}
