package com.rialms.assessment.item

import org.qtitools.qti.node.item.AssessmentItem

import org.springframework.beans.factory.InitializingBean

class ItemService implements InitializingBean {

    def grailsApplication;
    String contentPath;

    public AssessmentItem getAssessmentItem(Item e) {

        AssessmentItem assessmentItem = new AssessmentItem();
        assessmentItem.load(getItemDataFile(e));
        assessmentItem.initialize(null);
        return assessmentItem;
    }

    public File getItemDataFile(Item e) {
        return grailsApplication.parentContext.getResource("${getDataPath(e)}" + e.dataFile).getFile();
    }

    private String getDataPath(Item e) {
        return "${contentPath}/${e.dataPath}/"
    }

    public AssessmentItemInfo getAssessmentItemInfo(String itemId) {
        Item e = Item.get(itemId);

        return new AssessmentItemInfo(getAssessmentItem(e), getDataPath(e));
    }

    void afterPropertiesSet() {
        contentPath = grailsApplication.config.rialms.contentPath;
    }
}
