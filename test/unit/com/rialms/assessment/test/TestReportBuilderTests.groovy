package com.rialms.assessment.test

import static org.junit.Assert.*
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import org.springframework.core.io.ClassPathResource

/**
 * Created by IntelliJ IDEA.
 * User: Rajasekar Elango
 * Date: 3/29/12
 * Time: 10:57 PM
 * To change this template use File | Settings | File Templates.
 */

@TestMixin(GrailsUnitTestMixin)
class TestReportBuilderTests {

    void testBuildTestReport1() {
        String msg = "testBuildTestReport1 Failed";
        File inputFile = new ClassPathResource("data/TestBuildTestReportInputData1.xml").getFile()
        String title = 'Test title';
        TestReport actualTestReport = new TestReportBuilder().buildTestReport(title, inputFile.text);


        TestReport expectedTestReport = new TestReport(testTitle: title, summary: [duration: '10.813', 'part1.duration': '10.813', 'sectionA.duration': '10.813'],
                detail: [[item: 'math1', SCORE: '1.0'], [item: 'math2', SCORE: '1.0']])


        assertEquals(msg, expectedTestReport, actualTestReport);
    }

    void testBuildTestReport2() {
        String msg = "testBuildTestReport2 Failed";
        File inputFile = new ClassPathResource("data/TestBuildTestReportInputData2.xml").getFile()
        String title = 'Test title';
        TestReportBuilder reportBuilder = new TestReportBuilder();
        reportBuilder.setOutcomeVariablesToInclude(['SCORE', 'completionStatus'])

        TestReport actualTestReport = reportBuilder.buildTestReport(title, inputFile.text);


        TestReport expectedTestReport = new TestReport(testTitle: title, summary: ['duration': '18.119', 'part1.duration': '18.119', 'sectionA.duration': '6.001', 'sectionB.duration': '12.118'],
                detail: [[item: 'item034', SCORE: '1.0', completionStatus: 'unknown'], [item: 'item160', SCORE: '1.0', completionStatus: 'unknown'], [item: 'item063', SCORE: '', completionStatus: 'not_attempted'], [item: 'item347', SCORE: '0.0', completionStatus: 'unknown'], [item: 'item653', SCORE: '', completionStatus: 'not_attempted'], [item: 'item656', SCORE: '0.0', completionStatus: 'unknown']])


        assertEquals(msg, expectedTestReport, actualTestReport);
    }
}