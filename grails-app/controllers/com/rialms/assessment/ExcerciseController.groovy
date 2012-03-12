package com.rialms.assessment

import org.qtitools.qti.node.item.AssessmentItem ;
import  com.rialms.assessment.Exercise
import com.rialms.renderer.AssessmentItemRenderer;
import groovy.util.slurpersupport.GPathResult;

class ExerciseController {

    def grailsApplication;
    ExerciseService exerciseService;

    def index() { }

    def play(){
        String id = params.id;

        String dataFile = exerciseService.getExerciseDataFile(id)

        GPathResult xmlRoot = new XmlSlurper().parse(dataFile);
        println "${xmlRoot}"
        render(view: 'play', model:['xmlRoot':xmlRoot]);

    }
}
