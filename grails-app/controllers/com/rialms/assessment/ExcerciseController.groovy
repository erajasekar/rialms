package com.rialms.assessment

import org.qtitools.qti.node.item.AssessmentItem ;
import  com.rialms.assessment.Exercise
import com.rialms.renderer.AssessmentItemRenderer;
import groovy.util.slurpersupport.GPathResult
import com.rialms.util.UtilitiesService
import com.rialms.util.SampleParsing;

class ExerciseController {

    def grailsApplication;
    ExerciseService exerciseService;
    UtilitiesService utilitiesService ;

    def index() { }

    def play(){

        println "play params ${params}"
        def xmlRoot;
        AssessmentItem   assessmentItem;
        String  exercisePath;
        String id = params.id;
        if (id){
           String dataFile = exerciseService.getExerciseDataFile(id)
           xmlRoot = new XmlSlurper().parse(dataFile);
           session.exerciseXmlRoot = xmlRoot;
           session.assessmentItem = exerciseService.getAssessmentItem(id);
           session.exercisePath = exercisePath = exerciseService.getExercisePath(Exercise.get(id));
        }else{
           xmlRoot = session.exerciseXmlRoot;
           assessmentItem = session.assessmentItem;
           exercisePath = session.exercisePath;
        }
       println "exercisePath ${exercisePath}"
        //If enter pressed.
        Map<String,String> outcome = null;
        if (params.processButton.equals("Enter")){
            log.info("Processing Exercise");
            outcome = utilitiesService.processAssessmentItem(assessmentItem,params);
        }

        println "OUTCOME  => ${outcome} => ${outcome?.PROGRESS}"
        render(view: 'play', model:['xmlRoot':xmlRoot, 'outcome':outcome, 'exercisePath':exercisePath]);

    }

    def test(){
        String dataFile = exerciseService.getExerciseDataFile(params.id)
        GPathResult xmlRoot = new XmlSlurper().parse(dataFile);
      //  println xmlRoot
        println xmlRoot.itemBody;
        xmlRoot.itemBody.childNodes.each{ n ->
            println "==> ${n.name} ${n.text}";
        }
        render 'a';
    }
}
