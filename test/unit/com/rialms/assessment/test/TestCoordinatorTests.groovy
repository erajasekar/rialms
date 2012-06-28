/**
 * Created by IntelliJ IDEA.
 * User: relango
 * Date: 4/12/12
 * Time: 11:18 PM
 * To change this template use File | Settings | File Templates.
 */
package com.rialms.assessment.test

import static org.junit.Assert.*

import grails.test.mixin.*
import grails.test.mixin.support.*
import org.junit.*
import org.springframework.core.io.ClassPathResource
import com.rialms.consts.Constants as Consts
import com.rialms.assessment.item.AssessmentItemInfo;

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
class TestCoordinatorTests {

    public static final String TEST_FILE_NL_INDIVIDUAL = 'NonLinearIndividual.xml';
    public static final String TEST_FILE_L_INDIVIDUAL = 'LinearIndividual.xml';
    public static final String TEST_FILE_SIMULTANEOUS = 'Simultaneous.xml';
    public static final String TEST_FILE_DEEP_NESTED = 'deep-nested.xml';
    public static final String TEST_FILE_DEEP_NESTED_DISABLED_REVIEW = 'deep-nested-disabled-review.xml';
    public static final String TEST_FILE_ADAPTIVE_L_INDIVIDUAL = 'adaptive-linear-individual.xml';
    public static final String TEST_FILE_ADAPTIVE_NL_INDIVIDUAL = 'adaptive-nonlinear-individual.xml';
    public static final String TEST_FILE_SIMULTANEOUS_FEEDBACK = 'simultaneous-feedback.xml';
    public static final String TEST_FILE_L_INDIVIDUAL_TP_FEEDBACK = 'LinearIndividual-TestPartFeedback.xml';
    public static final String DATA_PATH = 'data/TestCoordinatorTests';

    void testGetNextQuestionForSubmitTestPartContent() {
        String msg = "testGetNextQuestionForSubmitTestPartContent Failed"
        TestCoordinator coordinator = createTestCoordinator(TEST_FILE_SIMULTANEOUS);
        coordinator.getNextQuestion(true);

        while (!coordinator.testController.hasNoMoreItemsInCurrentTestPart()) {
            coordinator.testRenderInfo;
            coordinator.getNextQuestion(true);
        }
        coordinator.getNextQuestion(true);
        TestRenderInfo testRenderInfo = coordinator.testRenderInfo;
        assertEquals("${msg} on submitTestPartContent param check", true, testRenderInfo.assessmentParams[Consts.submitTestPartContent])
    }

    void testRenderFeedbackContent() {
        String msg = "testRenderFeedbackContent Failed"
        TestCoordinator coordinator = createTestCoordinator(TEST_FILE_SIMULTANEOUS);
        coordinator.getNextQuestion(true);

        while (!coordinator.testController.hasNoMoreItemsInCurrentTestPart()) {
            coordinator.testRenderInfo;
            coordinator.getNextQuestion(true);
        }
        coordinator.doSimultaneousSubmission();
        //This test has two parts, so navigate next test part
        while (!coordinator.testController.hasNoMoreItemsInCurrentTestPart()) {
            coordinator.testRenderInfo;
            coordinator.getNextQuestion(true);
        }
        coordinator.doSimultaneousSubmission();
        coordinator.getNextQuestion(true);
        TestRenderInfo testRenderInfo = coordinator.testRenderInfo;
        assertEquals("${msg} on feedbackContent param check", true, testRenderInfo.assessmentParams[Consts.feedbackContent])
        assertTrue("${msg} on blank item check", testRenderInfo.assessmentItemInfo.is(AssessmentItemInfo.BLANK_ITEM))
    }

    void testNavigate() {
        String msg = "testNavigate Failed"
        doTestNavigate(msg, TEST_FILE_SIMULTANEOUS, true, 'math2');
        doTestNavigate(msg, TEST_FILE_NL_INDIVIDUAL, true, 'math2');

        //will remain on same item if response is not submitted
        doTestNavigate(msg, TEST_FILE_L_INDIVIDUAL, false, 'math1');

        //will navigate to next item only if response is submitted
        doTestNavigate(msg, TEST_FILE_L_INDIVIDUAL, true, 'math2', [:]);

        //For adaptive items, always same item should be rendered.
        doTestNavigate(msg, TEST_FILE_ADAPTIVE_L_INDIVIDUAL, false, 'perimeter1');
        doTestNavigate(msg, TEST_FILE_ADAPTIVE_NL_INDIVIDUAL, false, 'perimeter1', [:]);

        //item not submitted, so no feedback to render, so next item should be rendered
        doTestNavigate(msg, TEST_FILE_SIMULTANEOUS_FEEDBACK, true, 'perimeter1');
        //if item has feedback, same item should be rendered.
        doTestNavigate(msg, TEST_FILE_SIMULTANEOUS_FEEDBACK, false, 'feedback', [RESPONSE: 'MGH001A']);

        //if testPart has feedback, render same item.
        doTestNavigate(msg, TEST_FILE_L_INDIVIDUAL_TP_FEEDBACK, false, 'math1', [:]);

    }

    private void doTestNavigate(String msg, String inputFile, boolean expectedShouldRenderNextItem, String expectedItemRefId, Map response = null) {
        TestCoordinator coordinator = createTestCoordinator(inputFile);
        coordinator.getNextQuestion(true);
        coordinator.testRenderInfo;
        boolean shouldRenderNextItem;
        if (response != null) {
            //This will internally call navigate
            shouldRenderNextItem = coordinator.setCurrentResponse(response);
        } else {
            shouldRenderNextItem = coordinator.navigate();
        }

        TestRenderInfo testRenderInfo = coordinator.testRenderInfo;
        assertEquals("${msg} on shouldRenderNextItem check", expectedShouldRenderNextItem, shouldRenderNextItem);
        assertEquals("${msg} on item id check", expectedItemRefId, testRenderInfo.assessmentItemInfo.assessmentItemRef.identifier);
    }

    private TestCoordinator createTestCoordinator(String fileName) {
        File inputFile = new ClassPathResource("${DATA_PATH}${File.separator}${fileName}").getFile();
        TestCoordinator coordinator = new TestCoordinator(inputFile, DATA_PATH);
        return coordinator;
    }
}
