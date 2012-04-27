package com.rialms.assessment.test

import com.rialms.assessment.item.AssessmentItemInfo

import com.rialms.util.UtilitiesService
import grails.converters.JSON
import com.rialms.util.CollectionUtils
import com.rialms.consts.NavButton

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
            TestReport report = new TestReportBuilder().buildTestReport(coordinator.test.title, coordinator.getReport(), coordinator.testController.testStatus)
            render(view: 'report', model: [testReport: report]);
        }

    }

    def reset = {
        if (!params.id) {
            if (session.coordinator) {
                session.coordinator.clear()
            }
            return redirect(action: 'list')
        } else {
            if (session.coordinator) {
                session.coordinator.remove(params.id)
            }
            if (params.goto) {
                return redirect(action: params.goto)
            }
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

        if (params.containsKey("exit")) {
            params.goto = list;
            reset(params);
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
            testRenderInfo = testService.processAssessmentTest(params, coordinator);
        }

        if (testRenderInfo.assessmentParams.renderFeedbackContent) {
            render(view: 'feedback', model: testRenderInfo.toPropertiesMap());
            return;
        }

        // log.info("testRenderInfo  ==> ${testRenderInfo}");
        params.put('showInternalState', utilitiesService.showInternalState());
        render(view: 'play', model: testRenderInfo.toPropertiesMap())
    }

    def navigate = {
        log.info("Executing navigate with params ${params}");

        boolean renderNextItem = (params.renderNextItem) ?: true;

        if (!params.id) {
            return redirect(action: 'list')
        }
        //TODO session.coordianator is null
        TestCoordinator coordinator = session.coordinator[params.id]

        TestRenderInfo testRenderInfo = testService.processAssessmentTest(params, coordinator);

        log.info("testRenderInfo ==> ${testRenderInfo.assessmentParams}");

        if (testRenderInfo.assessmentParams.renderFeedbackContent) {
            render createRedirectLinkJSON(controller: 'test', action: 'feedback', params: params);
        } else {
            Map renderOutput = testRenderInfo.renderOutput;

            if (renderNextItem) {
                //To render next item, reset testContent
                if (testRenderInfo.assessmentParams.itemsPendingSubmission){
                    renderOutput.testContent = g.render(template: '/renderer/renderTestPartSubmission', model: testRenderInfo.toPropertiesMap());
                }else{
                    renderOutput.testContent = g.render(template: '/renderer/renderAssessmentItem', model: testRenderInfo.toPropertiesMap());
                }
            } else {

                //To render same item, just get render output for controls.
                renderOutput = CollectionUtils.mergeMapsByKeyAsList(coordinator.testController.currentItemInfo.renderOutput, renderOutput);
            }

            //Render if any test feedback
            renderOutput.testFeedback = g.render(template: '/renderer/renderTestFeedback', model: testRenderInfo.toPropertiesMap());
            log.info("Render Output ${renderOutput}");
            render renderOutput as JSON;
        }

    }

    def process() {
        log.info("Processing Test with param ${params}");

        if (!params.id) {
            return redirect(action: 'list')
        }
        //TODO session.coordianator is null
        TestCoordinator coordinator = session.coordinator[params.id]
        coordinator.setValidate(false);
        //submit should not be disabled automatically.
        log.info("Submiting answser for question Id ${params.questionId}");
        boolean renderNextItem = coordinator.setCurrentResponse(params);
        log.warn("renderNextItem ==> ${renderNextItem}");
        params.renderNextItem = renderNextItem;
        navigate(params);
    }



    def feedback() {
        log.info("Executing feedback with param ${params}");
        if (!params.id) {
            return redirect(action: 'list')
        }
        //TODO handle null value
        TestCoordinator coordinator = session.coordinator[params.id]
        TestRenderInfo testRenderInfo = coordinator.getTestRenderInfo();
        render(view: 'feedback', model: testRenderInfo.toPropertiesMap());
    }

    def submitTestPart() {
        log.info("Executing submitTestPart with param ${params}");
        if (!params.id) {
            return redirect(action: 'list')
        }
        //TODO handle null value
        TestCoordinator coordinator = session.coordinator[params.id]
        boolean renderNextItem = coordinator.doSimultaneousSubmission();
        params.renderNextItem = renderNextItem;
        log.info("submitTestPart ==> renderNextItem ==> ${renderNextItem}")
        navigate(params);
    }

    private JSON createRedirectLinkJSON(Map attributes) {
        Map<String, String> renderOutput = ['redirectUrl': createLink(attributes)];
        render renderOutput as JSON;
    }
}
