package com.rialms.assessment.item

import com.rialms.util.UtilitiesService

import org.qtitools.qti.validation.ValidationItem
import grails.web.JSONBuilder
import grails.converters.JSON

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
        List<ValidationItem> validationErrors = assessmentItemInfo.validate();
        if (!validationErrors.isEmpty()) {
            flash.validationErrors = validationErrors;
        }
        //If form submitted via post
        /* if (request.post) {
          log.info("Processing Item with param ${params}");
          assessmentItemInfo.processResponses(params);

      }  */
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
