package com.rialms.assessment

import com.rialms.assessment.test.TestCoordinator
import com.rialms.assessment.test.TestRenderInfo
import com.rialms.assessment.item.AssessmentItemInfo

import groovy.xml.XmlUtil

class TestController {

    TestService testService;

    def index() { }

    def list() {
        render "list";
    }

    def report() {
        log.info("Executing Report with params ${params}");
        //TODO modify getreport method itself to return xml instead of string.
        render session.coordinator[params.id].getReport();
    }

    def reset = {
        if (!params.id) {
            if (session.coordinator) session.coordinator.clear()
            return redirect(action: 'list')
        } else {
            def test = Test.get(params.id)
            if (!test) return redirect(action: 'list')
            if (session.coordinator) session.coordinator.remove(params.id)
            return redirect(action: 'play', id: params.id)
        }
    }

    def play() {

        if (!params.id) {
            return redirect(action: 'list')
        }
        if (params.containsKey("report")) {
            return redirect(action: 'report', id: params.id)
        }
        if (params.containsKey("exit")) {
            params.goto = list; reset(params)
        }

        TestCoordinator coordinator;
        TestRenderInfo testRenderInfo;

        if (!session.coordinator) session.coordinator = [:]

        if (!session.coordinator[params.id]) {
            coordinator = testService.createTestCoordinator(params.id);
            session.coordinator[params.id] = coordinator;
            testRenderInfo = coordinator.getTestRenderInfo();
        } else {
            coordinator = session.coordinator[params.id]
            coordinator.setValidate(false);
            if (coordinator.isCompleted()) {
                coordinator.getCurrentQuestion();
                testRenderInfo = coordinator.getTestRenderInfo();
            } else {
                testRenderInfo = testService.processAssessmentTest(params, coordinator);
            }

        }

        if (testRenderInfo.assessmentItemInfo.is(AssessmentItemInfo.BLANK_ITEM)) {
            render(view: 'feedback', model: testRenderInfo.toPropertiesMap());
            return;
        }
        //println "testRenderInfo properties " + testRenderInfo.toPropertiesMap();
        render(view: 'play', model: testRenderInfo.toPropertiesMap())
    }

    def feedback = {
        log.info("Executing feedback with params ${params} => ${chainModel}");
        render XmlUtil.serialize(chainModel.assessmentParams.assessmentFeedback);
    }
}
