package com.rialms.assessment.item

import com.rialms.util.UtilitiesService

import org.qtitools.qti.validation.ValidationItem
import grails.web.JSONBuilder
import grails.converters.JSON
import org.qtitools.qti.validation.ValidationResult

class ItemController {

    def grailsApplication;
    ItemService itemService;
    UtilitiesService utilitiesService;

    def index = { redirect(action: list, params: params) }

    def list = {
        if (!params.max) params.max = 50
        [itemList: Item.list(params)]

    }

    def play() {

        AssessmentItemInfo assessmentItemInfo;

        String id = params.id;
        if (id) {
            session.assessmentItemInfo = assessmentItemInfo = itemService.getAssessmentItemInfo(id);
        } else {
            assessmentItemInfo = session.assessmentItemInfo;
        }
        ValidationResult validationResult = assessmentItemInfo.validate();
        flash.validationResult = validationResult;

        params.put('showInternalState', utilitiesService.showInternalState());
        render(view: 'play', model: ['assessmentItemInfo': assessmentItemInfo]);

    }

    def process() {
        log.info("Processing Item with param ${params}");
        //TODO, see if we can eliminate session
        AssessmentItemInfo assessmentItemInfo = session.assessmentItemInfo;
        assessmentItemInfo.processResponses(params);
        Map renderOutput = assessmentItemInfo.renderOutput;
        //TODO
        log.info("Render Output ${renderOutput}");
        render renderOutput as JSON;
    }
}
