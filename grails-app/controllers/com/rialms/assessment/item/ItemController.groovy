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

        log.info("Playing Item with params ${params}");
        AssessmentItemInfo assessmentItemInfo;

        String id = params.id;
        if (!id) {
            redirect(action: 'list');

        } else {
            session[Consts.assessmentItemInfo] = assessmentItemInfo = itemService.getAssessmentItemInfo(id);
            ValidationResult validationResult = assessmentItemInfo.validate();
            flash[Consts.validationResult] = validationResult;

            params.put(Consts.showInternalState, utilitiesService.showInternalState());
            render(view: 'play', model: [(Consts.assessmentItemInfo): assessmentItemInfo]);
        }

    }

    def process() {
        log.info("Processing Item with param ${params}");
        AssessmentItemInfo assessmentItemInfo = session[Consts.assessmentItemInfo];
        Map renderOutput = [:];
        if (!assessmentItemInfo) {
            log.info("Session timed out");
            flash.message = g.message(code: 'session.timeout.error.message');
            renderOutput[Consts.redirectUrl] = g.createLink(action: 'play', params: [id: params.id]);
        } else {
            assessmentItemInfo.processResponses(params);
            renderOutput = assessmentItemInfo.renderOutput;
            if (assessmentItemInfo.isResponseValid) {
                renderOutput[Consts.angularData][Consts.itemResult] = qti.itemResult(assessmentItemInfo: assessmentItemInfo);
            }

        }
        log.info("Render Output ${renderOutput}");
        render renderOutput as JSON;
    }

    def viewItemXML() {
        log.info("Showing Item Xml with param ${params}");
        Map result = itemService.getItemXML(params.id);

        Map options = [(Consts.height): grailsApplication.config.rialms.qtiXMLDialogHeight, (Consts.width): 'auto', (Consts.modal): true, (Consts.closeButtonLabel): g.message('code': 'close.label')];//TODO P3 find better name/type for closeButtonLabel, in java it's used as boolean and string

        result[Consts.options] = result[Consts.options] + options;
        log.debug("DEBUG itemXML ${result}");
        render result as JSON;
    }
}
