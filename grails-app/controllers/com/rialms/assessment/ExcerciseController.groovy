package com.rialms.assessment

import org.qtitools.qti.node.item.AssessmentItem;
import com.rialms.assessment.Exercise
import com.rialms.renderer.AssessmentItemRenderer;
import groovy.util.slurpersupport.GPathResult
import com.rialms.util.UtilitiesService
import com.rialms.util.SampleParsing;

class ExerciseController {

    def grailsApplication;
    ExerciseService exerciseService;
    UtilitiesService utilitiesService;

    def index() { }

    def play() {

        Map<String, String> exerciseInfo;

        String id = params.id;
        if (id) {
            session.exerciseInfo = exerciseInfo = exerciseService.getExerciseInfo(id);
        } else {
            exerciseInfo = session.exerciseInfo;
        }

        def xmlRoot = exerciseInfo.xmlRoot;
        AssessmentItem assessmentItem = exerciseInfo.assessmentItem;
        def exercisePath = exerciseInfo.exercisePath;

        //If enter pressed.
        Map<String, String> outcome = null;
        if (params.processButton.equals("Enter")) {
            log.info("Processing Exercise with param ${params}");
            outcome = utilitiesService.processAssessmentItem(assessmentItem, params);
        }
        render(view: 'play', model: ['xmlRoot': xmlRoot, 'outcome': outcome, 'responseValues': assessmentItem.responseValues, 'exercisePath': exercisePath]);

    }

}
