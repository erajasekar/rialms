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

    void testBuildTestReport(){
        String msg = "testBuildTestReport Failed";
        File inputFile = new ClassPathResource("data/TestBuildTestReportInputData.xml").getFile()
        TestReport actualTestReport = TestReportBuilder.buildTestReport(inputFile.text);

        TestReport expectedTestReport = new TestReport(summary: [duration:'10.813', 'part1.duration':'10.813', 'sectionA.duration':'10.813'],
                                                       detail: [[item:'math1', SCORE:'1.0'], [item:'math2', SCORE:'1.0']])

        assertEquals(msg ,expectedTestReport,actualTestReport );

    }
}
