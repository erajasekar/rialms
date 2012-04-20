package com.rialms.assessment.test

import com.rialms.assessment.item.AssessmentItemInfo

import com.rialms.util.UtilitiesService
import grails.converters.JSON
import com.rialms.util.CollectionUtils

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

        if (testRenderInfo.assessmentItemInfo.is(AssessmentItemInfo.BLANK_ITEM)) {
            render(view: 'feedback', model: testRenderInfo.toPropertiesMap());
            return;
        }

        // log.info("testRenderInfo  ==> ${testRenderInfo}");
        params.put('showInternalState', utilitiesService.showInternalState());
        render(view: 'play', model: testRenderInfo.toPropertiesMap())
    }

    //TODO: clean up
    /* def process() {
        log.info("Processing Test with param ${params}");

        if (!params.id) {
            return redirect(action: 'list')
        }
        //TODO session.coordianator is null
        TestCoordinator coordinator = session.coordinator[params.id]
        coordinator.setValidate(false);
        log.info("Submiting answser for question Id ${params.questionId}");
        boolean renderSameItem = coordinator.setCurrentResponse(params);
        log.warn("renderSameItem ==> ${renderSameItem}");
        boolean renderNextItem = false;
        TestRenderInfo testRenderInfo;
        //log.info("NEXT ENABLED ==> ${coordinator.testController.nextEnabled()} === IS COMPLETE => ${coordinator.testController.currentItemInfo.isComplete()}")
        if (renderSameItem) {
            //   AssessmentItemInfo currentItemInfo = coordinator.testController.currentItemInfo;
            //TODO: This is actually redirecting to same page with enable/disable of controls, find better way
            // if (currentItemInfo.isComplete() || !coordinator.testController.submitEnabled()) {
            // renderNextItem = true;
            testRenderInfo = coordinator.getTestRenderInfo();
            //Render if any test feedback
            log.info("testRenderInfo  ==> ${testRenderInfo.assessmentParams}");
            Map renderOutput = coordinator.testController.currentItemInfo.renderOutput
            renderOutput = CollectionUtils.mergeMapsByKeyAsList(renderOutput, testRenderInfo.renderOutput);
            renderOutput.testFeedback = g.render(template: '/renderer/renderTestFeedback', model: testRenderInfo.toPropertiesMap());
            log.info("Render Output ${renderOutput}");
            render renderOutput as JSON;

            /*    }
          else {
              Map renderOutput = currentItemInfo.renderOutput
              log.info("Render Output ${renderOutput}");
              render renderOutput as JSON;
          }

        } else {
            renderNextItem = true;
        }
        if (renderNextItem) {
            // coordinator.getNextQuestion(false);
            /* String redirectUrl = createLink(controller: 'test', action: 'play', params: params);
         Map<String, String> renderOutput = ['redirectUrl': redirectUrl];
         render renderOutput as JSON;

            if (coordinator.isCompleted()) {
                coordinator.getCurrentQuestion();
                testRenderInfo = coordinator.getTestRenderInfo();
            } else {
                testRenderInfo = testService.processAssessmentTest(params, coordinator);
            }
            if (testRenderInfo.assessmentItemInfo.is(AssessmentItemInfo.BLANK_ITEM)) {
                //TODO refactor to common method
                String redirectUrl = createLink(controller: 'test', action: 'feedback', params: params);
                Map<String, String> renderOutput = ['redirectUrl': redirectUrl];
                render renderOutput as JSON;
            }
            //TODO fix timer last value
            log.info("testRenderInfo  ==> ${testRenderInfo.assessmentParams}");
            Map renderOutput = testRenderInfo.assessmentParams.navigationControls.visibleAndHiddenElementIds;
            renderOutput.testContent = g.render(template: '/renderer/renderTestContent', model: testRenderInfo.toPropertiesMap());
            renderOutput.testFeedback = g.render(template: '/renderer/renderTestFeedback', model: testRenderInfo.toPropertiesMap());
            log.info("Render Output ${renderOutput}");
            render renderOutput as JSON;
        }
    }*/

    def process() {
        log.info("Processing Test with param ${params}");

        if (!params.id) {
            return redirect(action: 'list')
        }
        //TODO session.coordianator is null
        TestCoordinator coordinator = session.coordinator[params.id]
        coordinator.setValidate(false);

        log.info("Submiting answser for question Id ${params.questionId}");
        boolean renderSameItem = coordinator.setCurrentResponse(params);
        log.warn("renderSameItem ==> ${renderSameItem}");
        TestRenderInfo testRenderInfo = testService.processAssessmentTest(params, coordinator);

        if (testRenderInfo.assessmentItemInfo.is(AssessmentItemInfo.BLANK_ITEM)) {
            render createRedirectLinkJSON(controller: 'test', action: 'feedback', params: params);
        }
        Map renderOutput = testRenderInfo.renderOutput;

        if (renderSameItem) {
            //To render same item, just get render output for controls.
            renderOutput = CollectionUtils.mergeMapsByKeyAsList(renderOutput, coordinator.testController.currentItemInfo.renderOutput);
        } else {
            //To render next item, reset testContent
            renderOutput.testContent = g.render(template: '/renderer/renderTestContent', model: testRenderInfo.toPropertiesMap());
        }

        log.info("testRenderInfo  ==> ${testRenderInfo}");
        //Render if any test feedback
        renderOutput.testFeedback = g.render(template: '/renderer/renderTestFeedback', model: testRenderInfo.toPropertiesMap());   //same
        log.info("Render Output ${renderOutput}");  //same
        render renderOutput as JSON;   //same

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

    private JSON createRedirectLinkJSON(Map attributes) {
        Map<String, String> renderOutput = ['redirectUrl': createLink(attributes)];
        render renderOutput as JSON;
    }
}
