package com.rialms.assessment

import com.rialms.util.UtilitiesService
import org.qtitools.qti.node.item.AssessmentItem

class ExerciseController {

    def grailsApplication;
    ExerciseService exerciseService;
    UtilitiesService utilitiesService;

    def index() { }

    def play() {

        Map exerciseInfo;

        String id = params.id;
        if (id) {
            session.exerciseInfo = exerciseInfo = exerciseService.getExerciseInfo(id);
        } else {
            exerciseInfo = session.exerciseInfo;
        }

        def xmlRoot = exerciseInfo.xmlRoot;
        AssessmentItem assessmentItem = exerciseInfo.assessmentItem;
        def dataPath = exerciseInfo.dataPath;

        //If enter pressed.
        AssessmentItemInfo assessmentItemInfo = new AssessmentItemInfo(assessmentItem);

        //TODO fix hint button name
        if (params.processButton == 'Enter') {
            log.info("Processing Exercise with param ${params}");
            assessmentItemInfo = utilitiesService.processAssessmentItem(assessmentItem, params);
        }

        render(view: 'play', model: ['xmlRoot': xmlRoot, 'assessmentItemInfo': assessmentItemInfo, 'dataPath': dataPath]);

    }

    def showHint = {

        log.info("Executing showHint with param ${params}");
        //TODO find alternative way to share exerciseInfo instead of session.
        Map exerciseInfo = session.exerciseInfo;
        AssessmentItem assessmentItem = exerciseInfo.assessmentItem;
        AssessmentItemInfo assessmentItemInfo = utilitiesService.processAssessmentItem(assessmentItem, params);
        render(view: 'play', model: ['xmlRoot': exerciseInfo.xmlRoot, 'assessmentItemInfo': assessmentItemInfo, 'dataPath': exerciseInfo.dataPath]);
    }

}
