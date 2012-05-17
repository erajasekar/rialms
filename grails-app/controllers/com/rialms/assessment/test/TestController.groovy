package com.rialms.assessment.test

import com.rialms.util.CollectionUtils
import com.rialms.util.UtilitiesService
import grails.converters.JSON
import com.rialms.consts.Constants as Consts
import sun.reflect.generics.scope.ConstructorScope

class TestController {

    TestService testService;
    UtilitiesService utilitiesService;

    def index() { redirect(action: list, params: params) }

    def list = {
        if (!params.max) params.max = 50
        [testList: Test.list(params)]
    }

    def report = {
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
            render(view: 'report', model: [(Consts.testReport): report]);
        }

    }

    def reset = {
        if (!params.id) {
            if (session[Consts.coordinator]) {
                session[Consts.coordinator].clear()
            }
            return redirect(action: 'list')
        } else {
            if (session[Consts.coordinator]) {
                session[Consts.coordinator].remove(params.id)
            }
            if (params[Consts.redirectto]) {
                return redirect(action: params[Consts.redirectto])
            }
            return redirect(action: 'play', id: params.id)
        }
    }

    def play() {

        log.info("Playing Test with param ${params}");
        if (!params.id) {
            return redirect(action: 'list')
        }

        TestCoordinator coordinator;
        TestRenderInfo testRenderInfo;

        if (!session[Consts.coordinator]) session[Consts.coordinator] = [:]

        if (!session[Consts.coordinator][params.id]) {
            coordinator = testService.createTestCoordinator(params.id);
            session[Consts.coordinator][params.id] = coordinator;
            testRenderInfo = coordinator.getTestRenderInfo();
        } else {
            coordinator = session[Consts.coordinator][params.id]
            coordinator.setValidate(false);
            testRenderInfo = testService.processAssessmentTest(params, coordinator);
        }

        if (testRenderInfo.assessmentParams[Consts.feedbackContent]) {
            render(view: 'feedback', model: testRenderInfo.toPropertiesMap());
            return;
        }

        log.info("testRenderInfo ==> ${testRenderInfo}");
        params.put(Consts.showInternalState, utilitiesService.showInternalState());
        render(view: 'play', model: testRenderInfo.toPropertiesMap())
    }

    def navigate = {
        log.info("Executing navigate with params ${params}");

        boolean renderNextItem = (params[Consts.renderNextItem]) ?: true;

        if (!params.id) {
            return redirect(action: 'list')
        }
        if (!session[Consts.coordinator][params.id]) {
            return redirect(action: 'play', params: params);
        }
        TestCoordinator coordinator = session[Consts.coordinator][params.id]

        TestRenderInfo testRenderInfo;

        testRenderInfo = testService.processAssessmentTest(params, coordinator);

        log.info("testRenderInfo ==> ${testRenderInfo[Consts.assessmentParams]}");

        if (testRenderInfo[Consts.assessmentParams][Consts.feedbackContent]) {
            render createRedirectLinkJSON(controller: 'test', action: 'feedback', params: params);
        } else {
            Map renderOutput = testRenderInfo.renderOutput;

            if (renderNextItem) {
                //To render next item, reset testContent
                if (testRenderInfo[Consts.assessmentParams][Consts.submitTestPartContent]) {
                    renderOutput[Consts.testContent] = g.render(template: '/renderer/renderTestPartSubmission', model: testRenderInfo.toPropertiesMap());
                } else {
                    renderOutput[Consts.testContent] = g.render(template: '/renderer/renderAssessmentItem', model: testRenderInfo.toPropertiesMap());
                    renderOutput[Consts.testNavigationStatus] = g.render(template: '/renderer/renderTestPartStatus', model: testRenderInfo.toPropertiesMap());
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
        if (!session[Consts.coordinator][params.id]) {
            return redirect(action: 'play', params: params);
        }
        TestCoordinator coordinator = session[Consts.coordinator][params.id]
        coordinator.setValidate(false);
        //submit should not be disabled automatically.
        log.info("Submiting answser for question Id ${params[Consts.questionId]}");
        boolean renderNextItem = coordinator.setCurrentResponse(params);
        log.warn("renderNextItem ==> ${renderNextItem}");
        params[Consts.renderNextItem] = renderNextItem;
        //We are navigating after submit and not after clicking nav button, so remove that parameter.
        params.remove(Consts.navButton);
        navigate(params);
    }



    def feedback() {
        log.info("Executing feedback with param ${params}");
        if (!params.id) {
            return redirect(action: 'list')
        }
        if (!session[Consts.coordinator][params.id]) {
            return redirect(action: 'play', params: params);
        }
        TestCoordinator coordinator = session[Consts.coordinator][params.id]
        TestRenderInfo testRenderInfo = coordinator.getTestRenderInfo();
        render(view: 'feedback', model: testRenderInfo.toPropertiesMap());
    }

    def submitTestPart() {
        log.info("Executing submitTestPartContent with param ${params}");
        if (!params.id) {
            return redirect(action: 'list')
        }
        if (!session?.coordinator[params.id]) {
            return redirect(action: 'play', params: params);
        }
        TestCoordinator coordinator = session[Consts.coordinator][params.id]
        boolean renderNextItem = coordinator.doSimultaneousSubmission();
        params[Consts.renderNextItem] = renderNextItem;
        log.info("submitTestPartContent ==> renderNextItem ==> ${renderNextItem}")
        navigate(params);
    }

    private JSON createRedirectLinkJSON(Map attributes) {
        Map<String, String> renderOutput = [(Consts.redirectUrl): createLink(attributes)];
        render renderOutput as JSON;
    }
}
