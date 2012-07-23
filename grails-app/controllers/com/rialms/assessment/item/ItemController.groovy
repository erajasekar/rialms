package com.rialms.assessment.item

import com.rialms.util.UtilitiesService

import org.qtitools.qti.validation.ValidationItem
import grails.web.JSONBuilder
import grails.converters.JSON
import org.qtitools.qti.validation.ValidationResult
import com.rialms.consts.Constants as Consts
import com.rialms.assessment.Feature

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
        if (!params.filterByFeature) params.filterByFeature = 'all'
        def itemList;

      //  if (params.filterByFeature == 'all') {
           // itemList = Item.list(params);
      //  } else {
            /*  Item item = Item.get(1);
          println "RAJA2 ${item.itemFeatures.collect{it.feature}}"
          def c = ItemFeature.createCriteria();
          Feature f = Feature.findByName(params.filterByFeature)
          List ifeatures = c.list {
              'eq'('feature',f)
          }
         //String query = """select item from Item,ItemFeature, Feature where Feature.name=:feature and ItemFe.feature_id = feature.id and item_feature.item_id=item.id """
          String query = "Select if from item_feature if, feature f where f.name='adaptive' and if.feature_id=f.id"
          List itemFeatures = ItemFeature.executeQuery(query);
          println "RAJA ${itemFeatures}"
          itemList = [];*/
            //println "RAJA ${itemList}"
            //itemList = itemFeatures.collect{it.item} as Set;
            def itemCriteria = Item.createCriteria();
            Closure filterCriteria = {
                if (params.filterByFeature != 'all'){
                    itemFeatures{
                        feature{
                            'eq'('name',params.filterByFeature)
                        }
                    }
                }
            }
            itemList = itemCriteria.list(params,filterCriteria);

       // }

        [itemList: itemList]
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
