package com.rialms.assessment.test

import com.rialms.consts.Constants as Consts

import com.rialms.consts.NavButton
import org.springframework.beans.factory.InitializingBean
import com.rialms.util.QtiUtils
import org.springframework.validation.Errors
import com.rialms.assessment.Feature
import grails.orm.PagedResultList
import com.rialms.assessment.item.Item
import com.rialms.consts.Constants
import org.apache.commons.lang.StringEscapeUtils
import org.qtitools.util.ContentPackage
import org.qtitools.qti.validation.ValidationResult
import org.qtitools.qti.node.test.AssessmentTest

class TestService implements InitializingBean {

    def grailsApplication;
    String contentPath;
    String demoTestsPath;
    def messageSource;
    int maxEntriesPerPage;
    def gspTagLibraryLookup;
    def g;

    public Test createTest(ContentPackage contentPackage){
      String dataPath = contentPackage.getDestination().absolutePath -  grailsApplication.parentContext.getResource(contentPath).getFile().absolutePath;
      String dataFile = contentPackage.getTest().name;
      return createTest(dataPath,dataFile);
    }

    public Test createTest(String dataPath, String dataFile) {
        File testXml = getTestDataFile(dataPath, dataFile);
        Test test = findOrCreateTest(dataPath, dataFile, testXml);
        if (dataPath.startsWith(demoTestsPath)) {
            addFeaturesToTest(test, QtiUtils.getFeaturesFromTestXml(testXml))
        }
        return test;
    }

    private Test findOrCreateTest(String dataPath, String dataFile, File testXml) {
        Test test = Test.findByDataPathAndDataFile(dataPath, dataFile, [cache: true])
        if (!test) {
            String testTitle = QtiUtils.getTitleFromXml(testXml);
            test = new Test(dataPath: dataPath, dataFile: dataFile, title: testTitle);
            test.save();
            if (test.hasErrors()) {
                test.errors.allErrors.each {log.error(messageSource.getMessage(it, null))}
            }
        }
        return test;
    }

    private void addFeaturesToTest(Test test, List<String> featureNames) {

        featureNames.each { featureName ->
            Feature feature = Feature.findByName(featureName);
            if (!feature) {
                log.error("Cannot add ${featureName} to test ${test.title}: invalid feature name")
            }
            createTestFeature(test, feature);
        }
    }

    private void createTestFeature(Test test, Feature feature) {
        if (!TestFeature.findByTestAndFeature(test, feature, [cache: true])) {
            TestFeature testFeature = new TestFeature(test: test, feature: feature);
            testFeature.save();
            if (testFeature.hasErrors()) {
                log.error("Error in creating Item Feature ${testFeature.errors}");
                testFeature.errors.allErrors.each {log.error(messageSource.getMessage(it, null))}
            }
        }
    }

    private File getTestDataFile(String dataPath, String dataFile) {
        return grailsApplication.parentContext.getResource("${getAbsoluteDataPath(dataPath)}" + dataFile).getFile();
    }

    private String getAbsoluteDataPath(String dataPath) {
        return "${contentPath}/${dataPath}/"
    }

    public String findTestIdByTitle(String title) {
        return Test.findByTitle(title).id;
    }

    public PagedResultList listTestsByFilter(Map params) {
        if (!params.max) params.max = maxEntriesPerPage
        if (!params.filterByFeature) params.filterByFeature = 'all'

        Closure filterCriteria = {
            if (params.filterByFeature != 'all') {
                testFeatures {
                    feature {
                        'eq'('name', params.filterByFeature)
                    }
                }
            } else {
                isNotEmpty('testFeatures')
            }
        }
        PagedResultList testList = Test.createCriteria().list(params, filterCriteria);

        return testList;
    }

    public TestCoordinator createTestCoordinator(String testId) {
        Test test = Test.get(testId);
        TestCoordinator coordinator = new TestCoordinator(getTestDataFile(test.dataPath, test.dataFile), getAbsoluteDataPath(test.dataPath), null);

        //render the first instance only with error report
        coordinator.setValidate(true);
        coordinator.getNextQuestion(false);
        return coordinator;
    }

    public ValidationResult validateTest(Test test){
        AssessmentTest assessmentTest = new AssessmentTest();
        assessmentTest.load(getTestDataFile(test.dataPath, test.dataFile));
        //TODO P0: handle exception
        assessmentTest.initialize();
        return assessmentTest.validate();
    }

    public TestRenderInfo processAssessmentTest(params, TestCoordinator coordinator) {
        //if params.id is set, use test from database, otherwise use session-bound test

        log.info("processAssessmentTest Params ${params}");

        if (params.containsKey(Consts.renderItem)) {
            coordinator.getQuestionByIdentifier(params.renderItem, params.isPositionedAfterCurrent.toBoolean());
        } else if (params.navButton == NavButton.next.name() && coordinator.getTestController().nextEnabled()) {
            coordinator.getNextQuestion(false);
        } else if (params.navButton == NavButton.forward.name() && coordinator.getTestController().forwardEnabled()) {
            coordinator.getNextQuestion(true);
        } else if (params.navButton == NavButton.previous.name() && coordinator.getTestController().previousEnabled()) {
            coordinator.getPreviousQuestion(false);
        } else if (params.navButton == NavButton.backward.name() && coordinator.getTestController().backwardEnabled()) {
            coordinator.getPreviousQuestion(true);
        } else if (params.navButton == NavButton.skip.name() && coordinator.getTestController().skipEnabled()) {
            coordinator.skipCurrentQuestion();
        } else if (coordinator.isCompleted()) {
            coordinator.getCurrentQuestion();
        }
        return coordinator.getTestRenderInfo()
    }

    public Map getTestXML(String id) {
        Map result = [:];
        log.debug("testId ${id}")
        String errMsg;
        if (id) {
            Test test = Test.get(Long.valueOf(id));
            if (test) {
                File testXml = getTestDataFile(test.dataPath, test.dataFile);
                result[Constants.content] = StringEscapeUtils.escapeHtml(testXml.text);
            } else {
                errMsg = "Invalid Test Id : ${id}"
            }
        }
        else {
            errMsg = "Test Id is null"
        }
        if (errMsg) {
            log.error("${errMsg}");
            result[Constants.content] = errMsg;
        }
        result[Constants.options] = [(Constants.title): g.message(['code': Constants.testXMLMessageCode])]
        result;
    }

    void afterPropertiesSet() {
        contentPath = grailsApplication.config.rialms.contentPath;
        maxEntriesPerPage = grailsApplication.config.rialms.maxEntriesPerPage
        demoTestsPath = grailsApplication.config.rialms.demoTestsPath;
        g = gspTagLibraryLookup.lookupNamespaceDispatcher("g")
    }

}
