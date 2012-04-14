package com.rialms.assessment.test

import groovy.util.logging.Log4j
import com.rialms.consts.AssessmentItemStatus

/**
 * Created by IntelliJ IDEA.
 * User: Rajasekar Elango
 * Date: 3/29/12
 * Time: 10:49 PM
 * To change this template use File | Settings | File Templates.
 */
@Log4j
class TestReportBuilder {

    private static final List<String> DEFAULT_OUTCOME_VARIABLES_TO_INCLUDE = ['SCORE'];

    private List<String> outcomeVariablesToInclude = DEFAULT_OUTCOME_VARIABLES_TO_INCLUDE;

    public TestReport buildTestReport(String testTitle, String xmlString, Map<String, EnumSet<AssessmentItemStatus>> testStatus) {
        def assessmentResult = new XmlSlurper().parseText(xmlString);
        def testResultDuration = assessmentResult.testResult.outcomeVariable.findAll { outcomeVariable -> outcomeVariable.@identifier =~ 'duration'}
        Map<String, String> summary = [:];
        testResultDuration.each {it ->
            summary[(it.@identifier.toString())] = it.text();
        }

        log.info("Test Report Summary ${summary}");

        List<Map<String, String>> detail = [];
        assessmentResult.itemResult.each { itemResult ->
            String itemResultId = itemResult.@identifier;
            Map<String, String> detailResult = [:];
            detailResult['item'] = itemResultId;
            def outcomeVariables = itemResult.outcomeVariable.findAll {outcomeVariable -> outcomeVariablesToInclude.contains(outcomeVariable.@identifier)}
            outcomeVariables.each { it ->
                detailResult[(it.@identifier.toString())] = it.text();
            }
            EnumSet<AssessmentItemStatus> statuses = testStatus?.get(itemResultId);
            detailResult.STATUS = (statuses) ? AssessmentItemStatus.format(statuses) : AssessmentItemStatus.format(AssessmentItemStatus.NOT_PRESENTED)

            detail << detailResult;
        }
        log.info("Test Report Detail ${detail}");
        return new TestReport(testTitle: testTitle, summary: summary, detail: detail);
    }

    public void setOutcomeVariablesToInclude(List<String> outcomeVariablesToInclude) {
        this.outcomeVariablesToInclude = outcomeVariablesToInclude
    }

    public List<String> getOutcomeVariablesToInclude() {
        return outcomeVariablesToInclude;
    }

}
