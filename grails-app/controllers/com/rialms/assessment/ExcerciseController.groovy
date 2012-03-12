package com.rialms.assessment

import org.qtitools.qti.node.item.AssessmentItem ;
import  com.rialms.assessment.Exercise
import com.rialms.renderer.AssessmentItemRenderer;
import com.rialms.renderer.AssessmentItemRendererService;

class ExerciseController {

    def grailsApplication;
    ExerciseService exerciseService;
    AssessmentItemRendererService assessmentItemRendererService;

    def index() { }

    def play(){
        String id = params.id;

        AssessmentItemRenderer renderer =  new AssessmentItemRenderer(exerciseService.getExerciseDataFile(id));
        //render(contentType: 'application/xhtml', encoding:"UTF-8", text:renderer.render(params)) ;
       // String content = renderer.render(params);
	String content = assessmentItemRendererService.render();
        println "${content}"
        render(view: 'play', model:['content':content]);

    }
}
