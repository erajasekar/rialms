import com.rialms.assessment.item.Item
import com.rialms.assessment.test.Test
import com.rialms.assessment.item.Item

class BootStrap {

    def init = { servletContext ->

        createItems();
        createTests()


    }
    def destroy = {
    }

    def createItems() {
        //1-5
        new Item(dataPath: 'exercise', dataFile: 'perimeter1.xml').save();
        new Item(dataPath: 'qti', dataFile: 'choice_fixed.xml').save();
        new Item(dataPath: 'qti', dataFile: 'choice_multiple.xml').save();
        new Item(dataPath: 'qti', dataFile: 'choice_multiple_chocolate.xml').save();
        new Item(dataPath: 'qti', dataFile: 'inline_choice.xml').save();

        //6-10
        new Item(dataPath: 'qti', dataFile: 'feedback.xml').save();
        new Item(dataPath: 'qti', dataFile: 'hint.xml').save();
        new Item(dataPath: 'qti', dataFile: 'adaptive.xml').save();
        new Item(dataPath: 'qti', dataFile: 'adaptive_template.xml').save();
        new Item(dataPath: 'qti', dataFile: 'template.xml').save();

        //11-15
        new Item(dataPath: 'qti', dataFile: 'template_image.xml').save();
        new Item(dataPath: 'qti', dataFile: 'math.xml').save();
        new Item(dataPath: 'qti', dataFile: 'MAB01a.xml').save();
        new Item(dataPath: 'qti', dataFile: 'MA-UT-01a-NoTP-Choice.xml').save();
        new Item(dataPath: 'qti', dataFile: 'MA-UT-01c-NoTP-Num.xml').save();

        //16-17
        new Item(dataPath: 'exercise/Std10', dataFile: 'Sequences_Ex_2_1.xml').save();
        new Item(dataPath: 'qti', dataFile: 'likert.xml').save();
        new Item(dataPath: 'qti', dataFile: 'mathml-templated.xml').save();
        new Item(dataPath: 'qti', dataFile: 'content-with-html-tags.xml').save();
    }

    def createTests() {

        //1-5
        new Test(dataPath: 'tests/qti/NonLinearSimpleTest', dataFile: 'assessment.xml').save();
        new Test(dataPath: 'tests/qti/Mathematics', dataFile: 'mathematics.xml').save();
        new Test(dataPath: 'tests/exercise/adaptive', dataFile: 'exercises.xml').save();
        new Test(dataPath: 'tests/qti/MathML-templated', dataFile: 'assessment.xml').save();
        new Test(dataPath: 'tests/qti/WebDeveloperTest1', dataFile: 'template_test1.xml').save();

        //6-10
        new Test(dataPath: 'tests/qti/NavigationMixed', dataFile: 'test-li-disabled_allowReview.xml').save();
        new Test(dataPath: 'tests/qti/NavigationMixed', dataFile: 'test-li-disabled_allowSkipping.xml').save();
        new Test(dataPath: 'tests/qti/NavigationMixed', dataFile: 'test-li-maxAttempts.xml').save();
        new Test(dataPath: 'tests/qti/NavigationMixed', dataFile: 'test-li-timeLimits_test.xml').save();
        new Test(dataPath: 'tests/qti/NavigationMixed', dataFile: 'test-ls-timeLimits_test.xml').save();

        //11-15
        new Test(dataPath: 'tests/qti/NavigationMixed', dataFile: 'test-nested-sections.xml').save();
        new Test(dataPath: 'tests/qti/NavigationMixed', dataFile: 'test-ni-disabled_allowReview.xml').save();
        new Test(dataPath: 'tests/qti/NavigationMixed', dataFile: 'test-ni-disabled_allowSkipping.xml').save();
        new Test(dataPath: 'tests/qti/NavigationMixed', dataFile: 'test-ni-maxAttempts.xml').save();
        new Test(dataPath: 'tests/qti/NavigationMixed', dataFile: 'test-ni-timeLimits_test.xml').save();

        //16-20
        new Test(dataPath: 'tests/qti/NavigationMixed', dataFile: 'test-ns-timeLimits_test.xml').save();
        new Test(dataPath: "tests/qti/NavigationMixed", dataFile: "test-li-outcome_expressions.xml").save();
        new Test(dataPath: "tests/qti/NavigationMixed", dataFile: "test-li-simple.xml").save();
        new Test(dataPath: "tests/qti/NavigationMixed", dataFile: "test-ls-simple.xml").save();
        new Test(dataPath: "tests/qti/NavigationMixed", dataFile: "test-ni-simple.xml").save();

        //21-25
        new Test(dataPath: "tests/qti/NavigationMixed", dataFile: "test-ns-simple.xml").save();
        new Test(dataPath: 'tests/qti/ModulePretest', dataFile: 'Test_Template.xml').save();
        new Test(dataPath: 'tests/navigation/individual', dataFile: 'individual_navigation.xml').save();
        new Test(dataPath: 'tests/navigation/simultaneous', dataFile: 'simultaneous_navigation.xml').save();
        new Test(dataPath: 'tests/qti/MultipleTestPartTest', dataFile: 'MultipleTestPartTest.xml').save();

    }
}
