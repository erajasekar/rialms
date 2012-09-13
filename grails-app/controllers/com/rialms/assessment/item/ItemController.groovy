package com.rialms.assessment.item

import com.rialms.consts.Constants as Consts

import com.rialms.util.UtilitiesService
import grails.converters.JSON
import org.qtitools.qti.validation.ValidationResult

class ItemController {

    def grailsApplication;
    ItemService itemService;
    UtilitiesService utilitiesService;

    def index = { redirect(action: list, params: params) }

    def list = {
        if (!params.max) params.max = 10
        //TODO: P3 This should return user uploaded items and not demo items
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
        if (assessmentItemInfo.isResponseValid){
            renderOutput[Consts.angularData][Consts.itemResult] = qti.itemResult(assessmentItemInfo:assessmentItemInfo);
        }
        log.info("Render Output ${renderOutput}");
        render renderOutput as JSON;
    }

    def viewItemXML() {
        log.info("Showing Item Xml with param ${params}");
        Map result = itemService.getItemXML(params.id);

        Map options = [(Consts.height):'600',(Consts.width):'auto', (Consts.modal):true, (Consts.closeButtonLabel) : g.message('code':'close.label')];

        result[Consts.options] = result[Consts.options] + options;
        log.info("DEBUG itemXML ${result}");
        render result as JSON;
    }
}
