package com.rialms.assessment

import com.rialms.assessment.test.TestCoordinator
import grails.converters.XML
import com.rialms.assessment.test.TestRenderInfo

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

    def reset() {
        render 'reset';
    }

    def play() {

        if (!params.id) return redirect(action: 'list')                     //no active test
        if (params.containsKey("report")) return redirect(action: report, id: params.id)    //show report
        if (params.containsKey("exit")) {params.goto = list; reset(params)}

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
            testRenderInfo = testService.processAssessmentTest(params, request, coordinator);
        }

        if (testRenderInfo == TestRenderInfo.NO_INFO) {
            log.info('testRenderInfo == TestRenderInfo.NO_INFO, redirecting to report');
            redirect(action: 'report', params: params);
        }
        println "testRenderInfo properties " + testRenderInfo.toPropertiesMap();
        render(view: 'play', model: testRenderInfo.toPropertiesMap())
    }
}
