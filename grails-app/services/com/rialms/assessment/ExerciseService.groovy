package com.rialms.assessment

import org.qtitools.qti.node.item.AssessmentItem

class ExerciseService {

    def grailsApplication;

    public AssessmentItem getAssessmentItem(String exerciseId) {

        assessmentItem =  new AssessmentItem();
		assessmentItem.load(grailsApplication.parentContext.getResource("${getExercisePath()}" + Exercise.get(exerciseId).dataFile).getFile());
		return assessmentItem;
    }

    public File getExerciseDataFile(String exerciseId){
         return grailsApplication.parentContext.getResource("${getExercisePath()}" + Exercise.get(exerciseId).dataFile).getFile();
    }

    //TODO move to after properties set;
    private String getExercisePath(){
           return grailsApplication.config.rialms.excercisePath;
    }
}
