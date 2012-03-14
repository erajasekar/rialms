package com.rialms.assessment

import org.qtitools.qti.node.item.AssessmentItem

class ExerciseService {

    def grailsApplication;

    public AssessmentItem getAssessmentItem(String exerciseId) {

        AssessmentItem assessmentItem =  new AssessmentItem();
     	assessmentItem.load(getExerciseDataFile(exerciseId));
		return assessmentItem;
    }

    public File getExerciseDataFile(String exerciseId){
         Exercise e =  Exercise.get(exerciseId);
         return grailsApplication.parentContext.getResource("${getExercisePath(e)}" + e.dataFile).getFile();
    }

    private String getExercisePath(Exercise e) {
        return "${getContentPath()}/${e.dataPath}/"
    }

    private String getExercisePathUri(String id){
         Exercise e =  Exercise.get(id);
         return grailsApplication.parentContext.getResource("${getExercisePath(e)}" + e.dataFile).getURI();
    }

    //TODO move to after properties set;
    private String getContentPath(){
           return grailsApplication.config.rialms.contentPath;
    }
}
