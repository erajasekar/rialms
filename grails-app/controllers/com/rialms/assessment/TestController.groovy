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
        render 'report';
    }

    def reset() {
        render 'reset';
    }

    def play() {

        if (!params.id) return redirect(action: 'list')                     //no active test
        if (params.containsKey("report")) return redirect(action: report, id: params.id)    //show report
        if (params.containsKey("exit")) {params.goto = list; reset(params)}

        TestRenderInfo testRenderInfo = testService.getTestRenderInfo(params, request);
        println testRenderInfo.toPropertiesMap();
        render(view: 'play', model: testRenderInfo.toPropertiesMap())
    }
}
