package com.rialms.assessment.item

import com.rialms.util.UtilitiesService

import org.qtitools.qti.validation.ValidationItem
import grails.web.JSONBuilder
import grails.converters.JSON
import org.qtitools.qti.validation.ValidationResult
import com.rialms.consts.Constants as Consts
import com.rialms.assessment.Feature
import com.rialms.consts.Constants

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
     //   Map result = itemService.getItemXML(params.id);

        Map result = [(Constants.html):"""<?xml version=\"1.0\" encoding=\"UTF-8\"?> <outcomeDeclaration identifier="SCORE" cardinality="single" baseType="float" normalMaximum="1.0">
        <defaultValue>
            <value>0</value>
        </defaultValue>
    </outcomeDeclaration>"""]
        result[Consts.options] = [:]; //TODO p1 remove
        Map options = [(Consts.height):'600',(Consts.width):'600', (Consts.modal):true];
        result[Consts.options] = result[Consts.options] + options;
        log.info("DEBUG itemXML ${result}");
        render result as JSON;
    }
}
