package com.rialms.assessment.test

import com.rialms.consts.Constants as Consts

import com.rialms.consts.NavButton
import org.springframework.beans.factory.InitializingBean

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

    void afterPropertiesSet() {
        contentPath = grailsApplication.config.rialms.contentPath;
    }

}
