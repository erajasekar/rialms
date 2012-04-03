package com.rialms.assessment.test

import org.springframework.beans.factory.InitializingBean
import com.rialms.assessment.test.TestCoordinator
import com.rialms.assessment.test.TestRenderInfo

import com.rialms.assessment.test.Test

class TestService implements InitializingBean {

    def grailsApplication;
    String contentPath;

    public File getTestDataFile(Test t) {
        return grailsApplication.parentContext.getResource("${getDataPath(t)}" + t.dataFile).getFile();
    }

    private String getDataPath(Test t) {
        return "${contentPath}/${t.dataPath}/"
    }

    public Map getTestInfo(String testId) {
        Test t = Test.get(testId);
        return [dataFile: getTestDataFile(t)];
    }

    public TestCoordinator createTestCoordinator(String testId) {
        Test test = Test.get(testId);
        TestCoordinator coordinator = new TestCoordinator(getTestDataFile(test), getDataPath(test), null);

        //TODO: Remove unused
        Map m = new HashMap();
        m.put("showInternalState", false);
        m.put("displayTitle", true);
        m.put("displayControls", true); //disable page controls
        coordinator.setPageRenderParameters(m);

        //render the first instance only with error report
        coordinator.setValidate(true);
        coordinator.getNextQuestion(false);
        return coordinator;
    }

    public TestRenderInfo processAssessmentTest(params, TestCoordinator coordinator) {
        //if params.id is set, use test from database, otherwise use session-bound test

        log.info("getRenderInfo Params ${params}");

        if (params.containsKey("next") && coordinator.getTestController().nextEnabled()) {
            coordinator.getNextQuestion(false);
        } else if (params.containsKey("forward") && coordinator.getTestController().forwardEnabled()) {
            coordinator.getNextQuestion(true);
        } else if (params.containsKey("previous") && coordinator.getTestController().previousEnabled()) {
            coordinator.getPreviousQuestion(false);
        } else if (params.containsKey("backward") && coordinator.getTestController().backwardEnabled()) {
            coordinator.getPreviousQuestion(true);
        } else if (params.containsKey("skip") && coordinator.getTestController().skipEnabled()) {
            coordinator.skipCurrentQuestion();
        } else if (coordinator.getTestController().isTestComplete()) {
            return coordinator.flashMessage("Oops, it appears that you have pressed the browsers back button! This is the question you should be viewing.");
        } else if (params.containsKey("questionId") || params.containsKey("submit")) { //TODO: check this condition for endAttemptInteracation, endAttemptInteraction attempts won't have submit set (but will always have questionId)
            //TODO
            log.info("Submiting answser for question Id ${params.questionId}");
            coordinator.setCurrentResponse(params);
        } else {
            log.warn("It appears that page was reloaded");
            //either a reload, or something more suspicious...
            //in any case, just redisplay the original page minus response
        }
        return coordinator.getTestRenderInfo()
    }

    void afterPropertiesSet() {
        contentPath = grailsApplication.config.rialms.contentPath;
    }

}
