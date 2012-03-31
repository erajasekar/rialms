import com.rialms.assessment.Exercise
import com.rialms.assessment.Test

class BootStrap {

    def init = { servletContext ->

        createExercises();
        createTests()


    }
    def destroy = {
    }

    def createExercises() {
        //1-5
        new Exercise(dataPath: 'exercise', dataFile: 'perimeter1.xml').save();
        new Exercise(dataPath: 'qti', dataFile: 'choice_fixed.xml').save();
        new Exercise(dataPath: 'qti', dataFile: 'choice_multiple.xml').save();
        new Exercise(title: 'choice multiple chocolate', dataPath: 'qti', dataFile: 'choice_multiple_chocolate.xml').save();
        new Exercise(title: 'inline choice', dataPath: 'qti', dataFile: 'inline_choice.xml').save();

        //6-10
        new Exercise(dataPath: 'qti', dataFile: 'feedback.xml').save();
        new Exercise(dataPath: 'qti', dataFile: 'hint.xml').save();
        new Exercise(dataPath: 'qti', dataFile: 'adaptive.xml').save();
        new Exercise(dataPath: 'qti', dataFile: 'adaptive_template.xml').save();
        new Exercise(dataPath: 'qti', dataFile: 'template.xml').save();

        //11-15
        new Exercise(dataPath: 'qti', dataFile: 'template_image.xml').save();
        new Exercise(dataPath: 'qti', dataFile: 'math.xml').save();
        new Exercise(dataPath: 'qti', dataFile: 'MAB01a.xml').save();
        new Exercise(dataPath: 'qti', dataFile: 'MA-UT-01a-NoTP-Choice.xml').save();
        new Exercise(dataPath: 'qti', dataFile: 'MA-UT-01c-NoTP-Num.xml').save();

        //16-17
        new Exercise(dataPath: 'exercise/Std10', dataFile: 'Sequences_Ex_2_1.xml').save();
        new Exercise(dataPath: 'qti', dataFile: 'likert.xml').save();
        new Exercise(dataPath: 'qti', dataFile: 'mathml-templated.xml').save();
        new Exercise(dataPath: 'qti', dataFile: 'content-with-html-tags.xml').save();
    }

    def createTests() {
        new Test(dataPath: 'tests/qti/NonLinearSimpleTest', dataFile: 'assessment.xml').save();
        new Test(dataPath: 'tests/qti/Mathematics', dataFile: 'mathematics.xml').save();
        new Test(dataPath: 'tests/exercise/adaptive', dataFile: 'exercises.xml').save();
        new Test(dataPath: 'tests/qti/MathML-templated', dataFile: 'assessment.xml').save();
        new Test(dataPath: 'tests/qti/WebDeveloperTest1', dataFile: 'template_test1.xml').save();

    }
}
