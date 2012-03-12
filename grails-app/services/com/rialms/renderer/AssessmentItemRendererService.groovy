package com.rialms.renderer

import com.rialms.assessment.Exercise

class AssessmentItemRendererService {

    def grailsApplication;

    def render() {
        def g = grailsApplication.mainContext.getBean('org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib')
        g.message(code:"default.boolean.true")
      //  g.each(in : "${Exercise.list()}"){
     //       ${it.dataFile}
     //   }
    }
}
