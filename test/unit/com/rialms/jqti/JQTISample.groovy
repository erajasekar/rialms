package com.rialms.jqti


import grails.test.GrailsUnitTestCase
import grails.test.*
import org.qtitools.qti.node.item.AssessmentItem
import org.qtitools.qti.node.RootNodeType
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: relango
 * Date: 3/15/12
 * Time: 5:23 PM
 * To change this template use File | Settings | File Templates.
 */
class JQTISample  {

    public static void main(String []args){
        //perimeterTest()
        choiceInteractionTest();
    }

    private static void perimeterTest(){
        File input = new File("c:/Raja/projects/rialms/web-app/content/exercise/perimeter1.xml");
                AssessmentItem assessmentItem = (AssessmentItem) RootNodeType.load(input);
                System.out.println(assessmentItem.toXmlString());

              //now test the item
                assessmentItem.initialize(null);
                assessmentItem.setResponses(["A" : ["3"]]);
                assessmentItem.setResponses(["B" : ["4"]]);
                assessmentItem.setResponses(["C" : ["5"]]);
                assessmentItem.processResponses();
                assessmentItem.setResponses(["SUM" : ["12"]]);
                assessmentItem.processResponses();
                System.out.println(assessmentItem.getOutcomeValues());
    }

    public static void choiceInteractionTest(){
        File input = new File("c:/Raja/projects/rialms/web-app/content/qti/choice_fixed.xml");
                        AssessmentItem assessmentItem = (AssessmentItem) RootNodeType.load(input);
                        System.out.println(assessmentItem.toXmlString());

                      //now test the item
                        assessmentItem.initialize(null);
                        Map responses = [RESPONSE : ["ChoiceA"]];
                        println "Response values ${responses}"
                        assessmentItem.setResponses(responses);
        println("Resp rules ${assessmentItem.getResponseProcessing().getTemplate()}");
                        assessmentItem.processResponses();
                        System.out.println(assessmentItem.getOutcomeValues());
    }
}
