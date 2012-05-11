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
    public static final String DATA_PATH = 'data/AssessmentTestControllerTests';

    @Ignore
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
        assertEquals("${msg} on testPartStatus count check", 6, testRenderInfo.assessmentParams[Consts.testPartStatus].values().flatten().size())
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


    private TestCoordinator createTestCoordinator(String fileName) {
        File inputFile = new ClassPathResource("${DATA_PATH}${File.separator}${fileName}").getFile();
        TestCoordinator coordinator = new TestCoordinator(inputFile, DATA_PATH);
        return coordinator;
    }
}
