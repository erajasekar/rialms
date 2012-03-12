package com.rialms.assessment

import org.qtitools.qti.node.item.AssessmentItem ;
import  com.rialms.assessment.Exercise;

class ExerciseController {

    def index() { }

    def play(){
        String id = params.id;
        AssessmentItem item = Exercise.get(id).getAssessmentItem()  ;
        String xml = item.toXmlString();
        render(contentType: 'application/xhtml+xml', text:xml) ;

    }
}
