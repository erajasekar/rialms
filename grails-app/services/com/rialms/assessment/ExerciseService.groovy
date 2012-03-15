package com.rialms.assessment

import org.qtitools.qti.node.item.AssessmentItem

class ExerciseService {

    def grailsApplication;

    public AssessmentItem getAssessmentItem(Exercise e) {

        AssessmentItem assessmentItem =  new AssessmentItem();
     	assessmentItem.load(getExerciseDataFile(e));
		return assessmentItem;
    }

    public File getExerciseDataFile(Exercise e){
         return grailsApplication.parentContext.getResource("${getExercisePath(e)}" + e.dataFile).getFile();
    }

    private String getExercisePath(Exercise e) {
        return "${getContentPath()}/${e.dataPath}/"
    }

    public Map<String,String> getExerciseInfo(String exerciseId){
        Exercise e = Exercise.get(exerciseId);

        return [xmlRoot:new XmlParser().parse(getExerciseDataFile(e)),
                assessmentItem:getAssessmentItem(e),
                exercisePath:getExercisePath(e)];
    }

    //TODO move to after properties set;
    private String getContentPath(){
           return grailsApplication.config.rialms.contentPath;
    }
}
