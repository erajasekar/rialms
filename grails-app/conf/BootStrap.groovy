import groovy.io.FileType

class BootStrap {

    def testService;
    def itemService;
    def featureService;
    def grailsApplication;

    def init = { servletContext ->
           initData();
    }
    def destroy = {
    }

    def initData(){
       featureService.createFeatures();
        createItems();
        createTests();
    }

    def createDemoItems(){
        String demoPath = grailsApplication.config.rialms.demoItemsPath;
        File demoDir = grailsApplication.parentContext.getResource(demoPath).getFile()
        log.info("Demo ${demoDir}")
        log.info("Demo ${demoDir.listFiles()}")
        demoDir.eachFile(FileType.FILES){ file ->
            itemService.createItem(demoPath,file.name);
        }

    }
    def createItems() {

        log.info("Initializing Item data...");
        createDemoItems();

        //1-5
        itemService.createItem('exercise','perimeter1.xml');
        itemService.createItem('qti','choice_fixed.xml');
        itemService.createItem('qti','choice_multiple.xml');
        itemService.createItem('qti','choice_multiple_chocolate.xml');
        itemService.createItem('qti','inline_choice.xml');

        //6-10
        itemService.createItem('qti','feedback.xml');
        itemService.createItem('qti','hint.xml');
        itemService.createItem('qti','adaptive.xml');
        itemService.createItem('qti','adaptive_template.xml');
        itemService.createItem('qti','template.xml');

        //11-15
        itemService.createItem('qti','template_image.xml');
        itemService.createItem('qti','math.xml');
        itemService.createItem('qti','MAB01a.xml');
        itemService.createItem('qti','MA-UT-01a-NoTP-Choice.xml');
        itemService.createItem('qti','MA-UT-01c-NoTP-Num.xml');

        //16-20
        itemService.createItem('exercise/Std10','Sequences_Ex_2_1.xml');
        itemService.createItem('qti','likert.xml');
        itemService.createItem('qti','mathml-templated.xml');
        itemService.createItem('qti','content-with-html-tags.xml');
        itemService.createItem('qti','order.xml');

        //21-25
        itemService.createItem('qti','match.xml');
        itemService.createItem('qti','associate.xml');
        itemService.createItem('qti','hottext.xml');
        itemService.createItem('qti','gap_match.xml');
    }

    def createTests() {

        log.info("Initializing Test data...");
        //1-5
        testService.createTest('tests/qti/NonLinearSimpleTest', 'assessment.xml');
        testService.createTest('tests/qti/Mathematics', 'mathematics.xml');
        testService.createTest('tests/exercise/adaptive', 'exercises.xml');
        testService.createTest('tests/qti/MathML-templated', 'assessment.xml');
        testService.createTest('tests/qti/WebDeveloperTest1', 'template_test1.xml');

        //6-10
        testService.createTest('tests/qti/NavigationMixed', 'test-li-disabled_allowReview.xml');
        testService.createTest('tests/qti/NavigationMixed', 'test-li-disabled_allowSkipping.xml');
        testService.createTest('tests/qti/NavigationMixed', 'test-li-maxAttempts.xml');
        testService.createTest('tests/qti/NavigationMixed', 'test-li-timeLimits_test.xml');
        testService.createTest('tests/qti/NavigationMixed', 'test-ls-timeLimits_test.xml');

        //11-15
        testService.createTest('tests/qti/NavigationMixed', 'test-nested-sections.xml');
        testService.createTest('tests/qti/NavigationMixed', 'test-ni-disabled_allowReview.xml');
        testService.createTest('tests/qti/NavigationMixed', 'test-ni-disabled_allowSkipping.xml');
        testService.createTest('tests/qti/NavigationMixed', 'test-ni-maxAttempts.xml');
        testService.createTest('tests/qti/NavigationMixed', 'test-ni-timeLimits_test.xml');

        //16-20
        testService.createTest('tests/qti/NavigationMixed', 'test-ns-timeLimits_test.xml');
        testService.createTest("tests/qti/NavigationMixed", "test-li-outcome_expressions.xml");
        testService.createTest("tests/qti/NavigationMixed", "test-li-simple.xml");
        testService.createTest("tests/qti/NavigationMixed", "test-ls-simple.xml");
        testService.createTest("tests/qti/NavigationMixed", "test-ni-simple.xml");

        //21-25
        testService.createTest("tests/qti/NavigationMixed", "test-ns-simple.xml");
        testService.createTest('tests/qti/ModulePretest', 'Test_Template-Individual-Submission.xml');
        testService.createTest('tests/qti/ModulePretest', 'Test_Template-Simultaneous-Submission.xml');
        testService.createTest('tests/navigation/individual', 'individual_navigation.xml');
        testService.createTest('tests/navigation/simultaneous', 'simultaneous_navigation.xml');


        //26-30
        testService.createTest('tests/qti/MultipleTestPartTest', 'MultipleTestPartTest-Simultaneous.xml');
        testService.createTest('tests/qti/MultipleTestPartTest', 'MultipleTestPartTest-Individual.xml');
        testService.createTest('tests/qti/MultipleTestPartTest', 'MultipleTestPartTest-Simultaneous-Timeout.xml');
        testService.createTest('tests/qti/MultipleTestPartTest', 'MultipleTestPartTest-Simultaneous-Individual.xml');
        testService.createTest("tests/qti/NavigationMixed", "test-ns-deep-nested.xml");

        //31-35
        testService.createTest("tests/qti/NavigationMixed", "test-ns-deep-nested-disabled-review.xml");
        testService.createTest('tests/qti/NavigationMixed', 'test-ns-disabled_allowReview.xml');
        testService.createTest('tests/qti/Interactions', 'interactions.xml');

    }
}
