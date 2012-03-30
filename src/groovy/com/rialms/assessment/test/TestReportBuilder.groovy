package com.rialms.assessment.test

/**
 * Created by IntelliJ IDEA.
 * User: Rajasekar Elango
 * Date: 3/29/12
 * Time: 10:49 PM
 * To change this template use File | Settings | File Templates.
 */
class TestReportBuilder {

    private static final List<String> OUTCOME_VARIABLES_TO_INCLUDE = ['SCORE'];
    
    public static TestReport buildTestReport(String testTitle,String xmlString){
        def assessmentResult = new XmlSlurper().parseText(xmlString);
        def testResultDuration = assessmentResult.testResult.outcomeVariable.findAll{ outcomeVariable -> outcomeVariable.@identifier =~ 'duration'}
        Map<String,String> summary = [:];
        testResultDuration.each{it ->
            summary[(it.@identifier)] = it.text();
        }

        println "test summary ${summary.getClass()} " + summary;

        List<Map<String,String>> detail = [];
        assessmentResult.itemResult.each { itemResult ->
             String itemResultId =  itemResult.@identifier;
             Map <String,String> outcomeVariableResult = [:];
             outcomeVariableResult['item'] = itemResultId;
             def outcomeVariables = itemResult.outcomeVariable.findAll {outcomeVariable -> OUTCOME_VARIABLES_TO_INCLUDE.contains(outcomeVariable.@identifier)}
             outcomeVariables.each{ it->
                 outcomeVariableResult[(it.@identifier)] = it.text();
             }
            detail << outcomeVariableResult;
        }
        println "test detail ${detail.getClass()} " + detail;
        return new TestReport(testTitle: testTitle, summary: summary, detail: detail);

    }

}
