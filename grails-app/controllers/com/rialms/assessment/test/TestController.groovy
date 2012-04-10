package com.rialms.assessment.test

import com.rialms.assessment.item.AssessmentItemInfo

import com.rialms.util.UtilitiesService
import grails.converters.JSON

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

        log.info("Playing Test with param ${params}");
        if (!params.id) {
            return redirect(action: 'list')
        }
        if (params.containsKey("report")) {
            return redirect(action: 'report', id: params.id)
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

        log.info("testRenderInfo  ==> ${testRenderInfo}");
        params.put('showInternalState', utilitiesService.showInternalState());
        render(view: 'play', model: testRenderInfo.toPropertiesMap())
    }

    def process() {
        log.info("Processing Test with param ${params}");
        //TODO
        //TODO appropriate redirects if id or session.coordianator is null
        TestCoordinator coordinator = session.coordinator[params.id]
        coordinator.setValidate(false);
        log.info("Submiting answser for question Id ${params.questionId}");
        boolean renderSameItem = coordinator.setCurrentResponse(params);
        log.warn("renderSameItem ==> ${renderSameItem}");
        boolean renderNextItem = false;
        if (renderSameItem) {
            AssessmentItemInfo currentItemInfo = coordinator.testController.currentItemInfo;
            if (currentItemInfo.isComplete()) {
                renderNextItem = true;
            }
            else {
                Map renderOutput = currentItemInfo.renderOutput
                log.info("Render Output ${renderOutput}");
                render renderOutput as JSON;
            }

        } else {
            renderNextItem = true;
        }
        if (renderNextItem) {
            String redirectUrl = createLink(controller: 'test', action: 'play', params: params);
            Map<String, String> renderOutput = ['redirectUrl': redirectUrl, 'params':params];
            render renderOutput as JSON;
        }
    }
}
