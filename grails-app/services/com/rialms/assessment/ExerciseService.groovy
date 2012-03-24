package com.rialms.assessment

import org.qtitools.qti.node.item.AssessmentItem
import com.rialms.assessment.item.AssessmentItemInfo
import org.springframework.beans.factory.InitializingBean

class ExerciseService implements InitializingBean {

    def grailsApplication;
    String contentPath;

    public AssessmentItem getAssessmentItem(Exercise e) {

        AssessmentItem assessmentItem = new AssessmentItem();
        assessmentItem.load(getExerciseDataFile(e));
        assessmentItem.initialize(null);
        return assessmentItem;
    }

    public File getExerciseDataFile(Exercise e) {
        return grailsApplication.parentContext.getResource("${getDataPath(e)}" + e.dataFile).getFile();
    }

    private String getDataPath(Exercise e) {
        return "${contentPath}/${e.dataPath}/"
    }

    public Map getExerciseInfo(String exerciseId) {
        Exercise e = Exercise.get(exerciseId);

        return [xmlRoot: new XmlParser().parse(getExerciseDataFile(e)),
                assessmentItemInfo: new AssessmentItemInfo(getAssessmentItem(e), getDataPath(e))];
    }

    void afterPropertiesSet() {
        contentPath = grailsApplication.config.rialms.contentPath;
    }
}
