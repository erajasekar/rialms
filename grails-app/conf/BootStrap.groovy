import com.rialms.assessment.Exercise

class BootStrap {

    def init = { servletContext ->

        new Exercise(title: 'Exercise 1 - Perimeter', dataPath: 'exercise', dataFile: 'perimeter1.xml').save();
        new Exercise(title: 'choice fixed', dataPath: 'qti', dataFile: 'choice_fixed.xml').save();
        new Exercise(title: 'choice multiple', dataPath: 'qti', dataFile: 'choice_multiple.xml').save();
        new Exercise(title: 'choice multiple chocolate', dataPath: 'qti', dataFile: 'choice_multiple_chocolate.xml').save();
        new Exercise(title: 'inline choice', dataPath: 'qti', dataFile: 'inline_choice.xml').save();

        new Exercise(title: 'feedback', dataPath: 'qti', dataFile: 'feedback.xml').save();
        new Exercise(title: 'hint', dataPath: 'qti', dataFile: 'hint.xml').save();
        new Exercise(title: 'adaptive', dataPath: 'qti', dataFile: 'adaptive.xml').save();
        new Exercise(title: 'adaptive template', dataPath: 'qti', dataFile: 'adaptive_template.xml').save();
        new Exercise(title: 'template', dataPath: 'qti', dataFile: 'template.xml').save();

        new Exercise(title: 'template image', dataPath: 'qti', dataFile: 'template_image.xml').save();


    }
    def destroy = {
    }
}
