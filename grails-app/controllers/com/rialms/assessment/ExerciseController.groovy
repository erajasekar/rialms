package com.rialms.assessment

import com.rialms.util.UtilitiesService
import org.qtitools.qti.node.item.AssessmentItem

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
        def dataPath = exerciseInfo.dataPath;

        //If enter pressed.
        AssessmentItemInfo assessmentItemInfo = new AssessmentItemInfo(assessmentItem);

        if (params.processButton.equals("Enter")) {
            log.info("Processing Exercise with param ${params}");
            assessmentItemInfo = utilitiesService.processAssessmentItem(assessmentItem, params);
        }

        render(view: 'play', model: ['xmlRoot': xmlRoot, 'assessmentItemInfo': assessmentItemInfo, 'dataPath': dataPath]);

    }

}
