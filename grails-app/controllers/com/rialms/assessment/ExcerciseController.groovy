package com.rialms.assessment

import org.qtitools.qti.node.item.AssessmentItem ;
import  com.rialms.assessment.Exercise
import com.rialms.renderer.AssessmentItemRenderer;
import groovy.util.slurpersupport.GPathResult
import com.rialms.util.UtilitiesService;

class ExerciseController {

    def grailsApplication;
    ExerciseService exerciseService;
    UtilitiesService utilitiesService ;

    def index() { }

    def play(){

        println "play params ${params}"
        GPathResult xmlRoot;
        AssessmentItem   assessmentItem;
        String id = params.id;
        if (id){
           String dataFile = exerciseService.getExerciseDataFile(id)
           xmlRoot = new XmlSlurper().parse(dataFile);
           session.exerciseXmlRoot = xmlRoot;
           session.assessmentItem = exerciseService.getAssessmentItem(id);
        }else{
           xmlRoot = session.exerciseXmlRoot;
           assessmentItem = session.assessmentItem;
        }

        //If enter pressed.
        Map<String,String> outcome = null;
        if (params.processButton.equals("Enter")){
            log.info("Processing Exercise");
            outcome = utilitiesService.processAssessmentItem(assessmentItem,params);
        }

        println "${xmlRoot}"
        render(view: 'play', model:['xmlRoot':xmlRoot, 'outcome':outcome]);

    }
}
