package com.rialms.assessment.item

import org.qtitools.qti.node.item.AssessmentItem
import com.rialms.assessment.item.AssessmentItemInfo
import org.springframework.beans.factory.InitializingBean
import com.rialms.assessment.item.Item

class ItemService implements InitializingBean {

    def grailsApplication;
    String contentPath;

    public AssessmentItem getAssessmentItem(Item e) {

        AssessmentItem assessmentItem = new AssessmentItem();
        assessmentItem.load(getExerciseDataFile(e));
        assessmentItem.initialize(null);
        return assessmentItem;
    }

    public File getExerciseDataFile(Item e) {
        return grailsApplication.parentContext.getResource("${getDataPath(e)}" + e.dataFile).getFile();
    }

    private String getDataPath(Item e) {
        return "${contentPath}/${e.dataPath}/"
    }

    public AssessmentItemInfo getAssessmentItemInfo(String exerciseId) {
        Item e = Item.get(exerciseId);

        return new AssessmentItemInfo(getAssessmentItem(e), getDataPath(e));
    }

    void afterPropertiesSet() {
        contentPath = grailsApplication.config.rialms.contentPath;
    }
}
