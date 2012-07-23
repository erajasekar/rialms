package com.rialms.assessment.item

import com.rialms.util.UtilitiesService

import org.qtitools.qti.validation.ValidationItem
import grails.web.JSONBuilder
import grails.converters.JSON
import org.qtitools.qti.validation.ValidationResult
import com.rialms.consts.Constants as Consts

class ItemController {

    def grailsApplication;
    ItemService itemService;
    UtilitiesService utilitiesService;

    def index = { redirect(action: list, params: params) }

    def list = {
        if (!params.max) params.max = 10
        [itemList: Item.list(params)]

    }

    def demo = {
        if (!params.max) params.max = 10
        [itemList: Item.list(params)]
    }

    def reset = {
        if (params[Consts.redirectto]) {
            return redirect(action: params[Consts.redirectto])
        }
        return redirect(action: 'play', id: params.id)
    }

    def play() {

        AssessmentItemInfo assessmentItemInfo;

        String id = params.id;
        if (id) {
            session[Consts.assessmentItemInfo] = assessmentItemInfo = itemService.getAssessmentItemInfo(id);
        } else {
            assessmentItemInfo = session.assessmentItemInfo;
        }
        ValidationResult validationResult = assessmentItemInfo.validate();
        flash[Consts.validationResult] = validationResult;

        params.put(Consts.showInternalState, utilitiesService.showInternalState());
        render(view: 'play', model: [(Consts.assessmentItemInfo): assessmentItemInfo]);

    }

    def process() {
        log.info("Processing Item with param ${params}");
        AssessmentItemInfo assessmentItemInfo = session[Consts.assessmentItemInfo];
        assessmentItemInfo.processResponses(params);
        Map renderOutput = assessmentItemInfo.renderOutput;
        log.info("Render Output ${renderOutput}");
        render renderOutput as JSON;
    }
}
