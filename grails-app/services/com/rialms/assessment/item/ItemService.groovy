package com.rialms.assessment.item

import org.qtitools.qti.node.item.AssessmentItem

import org.springframework.beans.factory.InitializingBean
import com.rialms.util.QtiUtils

class ItemService implements InitializingBean {

    def grailsApplication;
    String contentPath;

    public AssessmentItem getAssessmentItem(Item e) {

        AssessmentItem assessmentItem = new AssessmentItem();
        assessmentItem.load(getItemDataFile(e.dataPath,e.dataFile));
        assessmentItem.initialize(null);
        return assessmentItem;
    }

    public void createItem(String dataPath, String dataFile){
        //TODO P2 should create only if it doesn't exist
        String itemTitle = QtiUtils.getTitleFromXml(getItemDataFile(dataPath,dataFile));
        new Item(dataPath: dataPath,dataFile: dataFile, title: itemTitle).save();
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
    }
}
