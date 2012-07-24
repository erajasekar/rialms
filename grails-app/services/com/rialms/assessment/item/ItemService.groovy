package com.rialms.assessment.item

import org.qtitools.qti.node.item.AssessmentItem

import org.springframework.beans.factory.InitializingBean
import com.rialms.util.QtiUtils

import com.rialms.assessment.Feature
import grails.orm.PagedResultList

class ItemService implements InitializingBean {

    def grailsApplication;
    String contentPath;
    int maxEntriesPerPage;

    public AssessmentItem getAssessmentItem(Item e) {

        AssessmentItem assessmentItem = new AssessmentItem();
        assessmentItem.load(getItemDataFile(e.dataPath,e.dataFile));
        assessmentItem.initialize(null);
        return assessmentItem;
    }

    public void createItem(String dataPath, String dataFile){
        File itemXml = getItemDataFile(dataPath,dataFile)
        String itemTitle = QtiUtils.getTitleFromXml(itemXml);
        Item item = new Item(dataPath: dataPath,dataFile: dataFile, title: itemTitle);
        item.save();

        if (item.hasErrors()){
            log.warn("Errors in creating feature : ${item.errors}")
        }
        addFeaturesToItem(item, QtiUtils.getFeaturesFromItemXml(itemXml))
    }


    private void addFeaturesToItem(Item item, List<String> featureNames){

        featureNames.each{ featureName ->
            Feature feature = Feature.findByName(featureName);
            if (!feature){
                log.error("Cannot add ${featureName} to item ${item.title}: invalid feature name")
            }
            createItemFeature(item,feature);
        }
    }

    private void createItemFeature(Item item, Feature feature){
        ItemFeature itemFeature = new ItemFeature(item: item, feature: feature);
        log.debug("DEBUG creatingItemFeature ${item.title} -> ${feature.name}")
        itemFeature.save();
        if (itemFeature.hasErrors()){
            log.error("Error in creating Item Feature ${itemFeature.errors}");
        }
    }

    public PagedResultList listItemsByFilter(Map params){
        if (!params.max) params.max = maxEntriesPerPage
        if (!params.filterByFeature) params.filterByFeature = 'all'

        Closure filterCriteria = {
            if (params.filterByFeature != 'all'){
                itemFeatures{
                    feature{
                        'eq'('name',params.filterByFeature)
                    }
                }
            }
        }
        PagedResultList itemList = Item.createCriteria().list(params,filterCriteria);

        return itemList;
    }

    private File getItemDataFile(String dataPath, String dataFile) {
        return grailsApplication.parentContext.getResource("${getAbsoluteDataPath(dataPath)}" + dataFile).getFile();
    }

    private String getAbsoluteDataPath(String dataPath) {
        return "${contentPath}/${dataPath}/"
    }

    public AssessmentItemInfo getAssessmentItemInfo(String itemId) {
        Item e = Item.get(itemId);
        return new AssessmentItemInfo(getAssessmentItem(e),getAbsoluteDataPath(e.dataPath));
    }

    void afterPropertiesSet() {
        contentPath = grailsApplication.config.rialms.contentPath;
        maxEntriesPerPage = grailsApplication.config.rialms.maxEntriesPerPage
    }
}
