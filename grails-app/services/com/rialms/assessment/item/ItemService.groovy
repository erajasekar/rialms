package com.rialms.assessment.item

import org.qtitools.qti.node.item.AssessmentItem

import org.springframework.beans.factory.InitializingBean
import com.rialms.util.QtiUtils

import com.rialms.assessment.Feature
import grails.orm.PagedResultList
import sun.reflect.generics.scope.ConstructorScope
import com.rialms.consts.Constants
import org.apache.commons.lang.StringEscapeUtils


class ItemService implements InitializingBean {

    def grailsApplication;
    String contentPath;
    String demoItemsPath;
    int maxEntriesPerPage;
    def gspTagLibraryLookup;
    def messageSource;
    def g;

    public AssessmentItem getAssessmentItem(Item e) {

        AssessmentItem assessmentItem = new AssessmentItem();
        assessmentItem.load(getItemDataFile(e.dataPath, e.dataFile));
        //Note: Fixed bug in JQTI AssessmentItem.setCompletionStatus to set value to IndentifierValue instead of StringValue
        //Recompiled and new jar.
        assessmentItem.initialize(null);
        return assessmentItem;
    }

    public void createItem(String dataPath, String dataFile) {
        File itemXml = getItemDataFile(dataPath, dataFile)

        if (dataPath == demoItemsPath) {
            addFeaturesToItem(findOrCreateItem(dataPath,dataFile, itemXml), QtiUtils.getFeaturesFromItemXml(itemXml))
        }

    }

    private Item findOrCreateItem(String dataPath, String dataFile, File itemXml){
        Item item = Item.findByDataPathAndDataFile(dataPath,dataFile, [cache : true])
        if (!item){
            String itemTitle = QtiUtils.getTitleFromXml(itemXml);
            item = new Item(dataPath: dataPath, dataFile: dataFile, title: itemTitle);
            item.save();
            log.debug("DEBUG creating Item with dataPath : ${dataPath} , dataFile : ${dataFile} , itemTitle , ${itemTitle}")
            if (item.hasErrors()) {
                item.errors.allErrors.each {log.error(messageSource.getMessage(it, null))}
            }
        }
        return item;
    }


    private void addFeaturesToItem(Item item, List<String> featureNames) {

        featureNames.each { featureName ->
            Feature feature = Feature.findByName(featureName);
            if (!feature) {
                log.error("Cannot add ${featureName} to item ${item.title}: invalid feature name")
            }
            createItemFeature(item, feature);
        }
    }

    private void createItemFeature(Item item, Feature feature) {
        if (!ItemFeature.findByItemAndFeature(item,feature, [cache: true])){
            ItemFeature itemFeature = new ItemFeature(item: item, feature: feature);
            log.debug("DEBUG creatingItemFeature ${item.title} -> ${feature.name}")
            itemFeature.save();
            if (itemFeature.hasErrors()) {
                log.error("Error in creating Item Feature ${itemFeature.errors}");
            }
        }
    }

    public PagedResultList listItemsByFilter(Map params) {
        if (!params.max) params.max = maxEntriesPerPage
        if (!params.filterByFeature) params.filterByFeature = 'all'

        Closure filterCriteria = {
            if (params.filterByFeature != 'all') {
                itemFeatures {
                    feature {
                        'eq'('name', params.filterByFeature)
                    }
                }
            } else {
                isNotEmpty('itemFeatures')
            }
        }
        PagedResultList itemList = Item.createCriteria().list(params, filterCriteria);

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
        return new AssessmentItemInfo(getAssessmentItem(e), getAbsoluteDataPath(e.dataPath));
    }

    void afterPropertiesSet() {
        contentPath = grailsApplication.config.rialms.contentPath;
        maxEntriesPerPage = grailsApplication.config.rialms.maxEntriesPerPage
        demoItemsPath = grailsApplication.config.rialms.demoItemsPath
        g = gspTagLibraryLookup.lookupNamespaceDispatcher("g")
    }

    public Map getItemXML(String id) {
        Map result = [:];
        log.debug("itemId ${id}")
        String errMsg;
        if (id) {
            Item item = Item.get(Long.valueOf(id));
            if (item) {
                File itemXml = getItemDataFile(item.dataPath, item.dataFile);
                result[Constants.content] = StringEscapeUtils.escapeHtml(itemXml.text);
            } else {
                errMsg = "Invalid Item Id : ${id}"
            }
        }
        else {
            errMsg = "Item Id is null"
        }
        if (errMsg) {
            log.error("${errMsg}");
            result[Constants.content] = errMsg;
        }
        result[Constants.options] = [(Constants.title):g.message(['code':Constants.itemXMLMessageCode])]
        result;
    }
}
