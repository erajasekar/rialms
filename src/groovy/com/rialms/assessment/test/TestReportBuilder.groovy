package com.rialms.assessment.test

import groovy.util.logging.Log4j
import com.rialms.consts.AssessmentItemStatus
import com.rialms.consts.Constants as Consts

/**
 * Created by IntelliJ IDEA.
 * User: Rajasekar Elango
 * Date: 3/29/12
 * Time: 10:49 PM
 * To change this template use File | Settings | File Templates.
 */
@Log4j
class TestReportBuilder {

    private static final List<String> DEFAULT_ITEM_OUTCOME_VARIABLES_TO_INCLUDE = ['SCORE'];
    private static final List<String> DEFAULT_TEST_OUTCOME_VARIABLES_TO_INCLUDE = ['SCORE', 'duration'];

    private List<String> itemOutcomeVariablesToInclude = DEFAULT_ITEM_OUTCOME_VARIABLES_TO_INCLUDE;
    private List<String> testOutcomeVariablesToInclude = DEFAULT_TEST_OUTCOME_VARIABLES_TO_INCLUDE;

    public TestReport buildTestReport(String testTitle, String xmlString, Map<String, AssessmentItemStatus> testStatus) {

        log.info("Report xmlString ${xmlString}");
        def assessmentResult = new XmlSlurper().parseText(xmlString);
        def testResultSummary = assessmentResult.testResult.outcomeVariable.findAll { outcomeVariable -> testOutcomeVariablesToInclude.find{variable -> outcomeVariable.@identifier =~ variable}}
        Map<String, String> summary = [:];
        testResultSummary.each {it ->
            summary[(it.@identifier.toString())] = it.text();
        }

        log.info("Test Report Summary ${summary}");

        List<Map<String, String>> detail = [];
        assessmentResult.itemResult.each { itemResult ->
            String itemResultId = itemResult.@identifier;
            Map<String, String> detailResult = [:];
            detailResult[Consts.item] = itemResultId;
            def outcomeVariables = itemResult.outcomeVariable.findAll {outcomeVariable -> itemOutcomeVariablesToInclude.contains(outcomeVariable.@identifier)}
            outcomeVariables.each { it ->
                detailResult[(it.@identifier.toString())] = it.text();
            }
            AssessmentItemStatus status = testStatus?.get(itemResultId);
            detailResult[Consts.STATUS] = (status) ? status :AssessmentItemStatus.NOT_PRESENTED

            detail << detailResult;
        }
        log.info("Test Report Detail ${detail}");
        return new TestReport(testTitle: testTitle, summary: summary, detail: detail);
    }

    public void setOutcomeVariablesToInclude(List<String> outcomeVariablesToInclude) {
        this.itemOutcomeVariablesToInclude = outcomeVariablesToInclude
    }

    public List<String> getOutcomeVariablesToInclude() {
        return itemOutcomeVariablesToInclude;
    }

}
