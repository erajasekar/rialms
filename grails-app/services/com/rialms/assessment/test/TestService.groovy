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

    public TestCoordinator createTestCoordinator(String testId) {
        Test test = Test.get(testId);
        TestCoordinator coordinator = new TestCoordinator(getTestDataFile(test), getDataPath(test), null);

        //render the first instance only with error report
        coordinator.setValidate(true);
        coordinator.getNextQuestion(false);
        return coordinator;
    }

    public TestRenderInfo processAssessmentTest(params, TestCoordinator coordinator) {
        //if params.id is set, use test from database, otherwise use session-bound test

        log.info("processAssessmentTest Params ${params}");

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
        } else if (coordinator.isCompleted()) {
            coordinator.getCurrentQuestion();
        }
        return coordinator.getTestRenderInfo()
    }

    void afterPropertiesSet() {
        contentPath = grailsApplication.config.rialms.contentPath;
    }

}
