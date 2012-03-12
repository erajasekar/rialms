package com.rialms.renderer

import groovy.util.slurpersupport.GPathResult
import groovy.xml.MarkupBuilder

/**
 * com.rialms.renderer
 *
 * Created on 3/11/12 . 9:45 PM
 * @Author E. Rajasekar 
 *
 */
class AssessmentItemRenderer {

    private GPathResult xml;

    public AssessmentItemRenderer(File dataFile) {
        GPathResult xml = new XmlSlurper().parse(dataFile);
    }

    public String render(Map params) {
        StringWriter sw = new StringWriter()
        MarkupBuilder builder = new groovy.xml.MarkupBuilder(sw)
        builder.html {
            renderHead(builder);
            renderBody(builder,params);
        }

        return sw.toString();
    }

    private void renderHead(builder){
          builder.head{
                title "XML encoding with Groovy"
                meta(name:'layout' , content:'main');
          }
    }

    private void renderBody(builder, Map params){
           builder.body{
               def g = new org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib() ;
               String msg = g.message(message : 'my custom msg"');
               builder.p("SUCCESS");
               builder.p(msg);
           }
    }
}
