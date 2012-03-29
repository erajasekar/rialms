package com.rialms.assessment

import com.rialms.util.UtilitiesService
import org.qtitools.qti.node.item.AssessmentItem
import grails.converters.XML
import com.rialms.assessment.item.AssessmentItemInfo

class ExerciseController {

    def grailsApplication;
    ExerciseService exerciseService;
    UtilitiesService utilitiesService;

    def index = { redirect(action: list, params: params) }

    def list = {
        if (!params.max) params.max = 50
        [exerciseList: Exercise.list(params)]

    }

    def play() {

        AssessmentItemInfo assessmentItemInfo;

        String id = params.id;
        if (id) {
            session.assessmentItemInfo = assessmentItemInfo = exerciseService.getAssessmentItemInfo(id);
        } else {
            assessmentItemInfo = session.assessmentItemInfo;
        }

        //If form submitted via post
        if (request.post) {
            log.info("Processing Exercise with param ${params}");
            assessmentItemInfo.processResponses(params);

        }

        render(view: 'play', model: ['assessmentItemInfo': assessmentItemInfo]);

    }
}
