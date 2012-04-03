package com.rialms.assessment

import com.rialms.assessment.test.TestCoordinator
import com.rialms.assessment.test.TestRenderInfo
import com.rialms.assessment.item.AssessmentItemInfo

import groovy.xml.XmlUtil
import com.rialms.assessment.test.TestReport
import com.rialms.assessment.test.TestReportBuilder
import com.rialms.util.UtilitiesService

class TestController {

    TestService testService;
    UtilitiesService utilitiesService;

    def index() { }

    def list = {
        if (!params.max) params.max = 50
        [testList: Test.list(params)]
    }

    def report() {
        log.info("Executing Report with params ${params}");
        if (!params.id) {
            redirect(action: 'list')
        }
        else if (!session.coordinator[params.id]) {
            flash.message = 'test.report.noreport.error'
            redirect(action: 'play', params: params);
        } else {
            TestCoordinator coordinator = session.coordinator[params.id];
            TestReport report = new TestReportBuilder().buildTestReport(coordinator.test.title, coordinator.getReport())
            render(view: 'report', model: [testReport: report]);
        }

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

        params.put('showInternalState', utilitiesService.showInternalState());
        render(view: 'play', model: testRenderInfo.toPropertiesMap())
    }

}
