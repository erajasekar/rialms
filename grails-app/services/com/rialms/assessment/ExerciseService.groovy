package com.rialms.assessment

import org.qtitools.qti.node.item.AssessmentItem

class ExerciseService {

    def grailsApplication;

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
        return "${getContentPath()}/${e.dataPath}/"
    }

    public Map getExerciseInfo(String exerciseId) {
        Exercise e = Exercise.get(exerciseId);

        return [xmlRoot: new XmlParser().parse(getExerciseDataFile(e)),
                assessmentItemInfo: new AssessmentItemInfo(getAssessmentItem(e),getDataPath(e))];
    }

    //TODO move to after properties set;
    private String getContentPath() {
        return grailsApplication.config.rialms.contentPath;
    }


}
