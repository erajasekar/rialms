package com.rialms.assessment.test

import static org.junit.Assert.*

import grails.test.mixin.*
import grails.test.mixin.support.*
import org.junit.*
import org.springframework.core.io.ClassPathResource
import static org.junit.Assert.assertNull
import static org.junit.Assert.assertNull
import org.qtitools.qti.node.item.AssessmentItem
import org.qtitools.qti.node.test.AssessmentItemRef
import com.rialms.assessment.item.AssessmentItemInfo
import com.rialms.consts.AssessmentItemStatus

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
class AssessmentTestControllerTests {

    public static final String TEST_FILE_INDIVIDUAL = 'Individual.xml';
    public static final String TEST_FILE_SIMULTANEOUS = 'Simultaneous.xml';
    public static final String TEST_FILE_DEEP_NESTED = 'deep-nested.xml';

    @Ignore
    void testGetItemByIdentifier() {
        String msg = "testGetItemByIdentifier Failed ";
        AssessmentTestController test = createTestController(TEST_FILE_INDIVIDUAL);

        assertEquals("${msg} on check find next item by identifier", 'math5', test.getItemByIdentifier('math5', true)?.identifier);
        assertEquals("${msg} on check find previous item by identifier", 'math1', test.getItemByIdentifier('math1', false)?.identifier);

        //should find across testParts
        assertEquals("${msg} on check find next item by identifier for different test part", 'math10', test.getItemByIdentifier('math10', true)?.identifier);

        //can'f find across testsParts, so should be null
        assertNull("${msg} on check find previous item by identifier for different test part", test.getItemByIdentifier('math1', false));

        assertNull("${msg} on check find next item by identifier for invalid identifier", test.getItemByIdentifier('mathx', true));
        assertNull("${msg} on check find previous item by identifier for invalid identifier", test.getItemByIdentifier('mathx', false));

    }

    @Ignore
    void testGetCurrentItemInfo() {
        String msg = "testGetCurrentItemInfo Failed ";
        AssessmentTestController test = createTestController(TEST_FILE_INDIVIDUAL);

        Map presented = [:]
        //navigate couple on items
        3.times {
            test.getNextItem(false);
            AssessmentItemInfo itemInfo = test.getCurrentItemInfo();
            assertFalse("${msg}, new instance of itemInfo was not created", presented.values().contains(itemInfo))
            assertEquals("${msg} on status check ", AssessmentItemStatus.PRESENTED, itemInfo.itemStatus)
            presented[itemInfo.assessmentItemRef.identifier] = itemInfo;
        }
        //navigate back to already presented item
        test.getItemByIdentifier('math2', false);
        AssessmentItemInfo itemInfo = test.getCurrentItemInfo();
        assertEquals("${msg}, did not return existing instance from processedItems ", presented[itemInfo.assessmentItemRef.identifier], itemInfo)
    }

    @Ignore
    void testIsTestComplete() {
        String msg = "testIsTestComplete Failed ";
        doTestIsComplete(msg, TEST_FILE_INDIVIDUAL);
        doTestIsComplete(msg, TEST_FILE_SIMULTANEOUS);
    }

    void testGetCurrentTestPartStatus() {
        String msg = "testGetItemByIdentifier Failed ";
        AssessmentTestController test = createTestController(TEST_FILE_DEEP_NESTED);
        test.getNextItem(true);
        while (test.hasNoMoreItemsInCurrentTestPart()) {
            test.getCurrentItemInfo();
            test.getNextItem(true);
            //TODO test for current item
        }

        test.getCurrentTestPartStatus().each { k, v ->
            println "${k} ===> ${v}";
        }
    }

    private void doTestIsComplete(String msg, String inputFile) {
        AssessmentTestController test = createTestController(inputFile);

        //navigate until last item
        while (test.getNextItem(true) != null) {
            assertFalse("${msg}, test should not be complete", test.isTestComplete());
        }
        assertTrue("${msg}, test should be complete", test.isTestComplete());
    }

    private AssessmentTestController createTestController(String fileName) {
        File inputFile = new ClassPathResource("data/AssessmentTestControllerTests/Individual.xml").getFile();
        AssessmentTestController test = new AssessmentTestController(inputFile, "data/AssessmentTestControllerTests");
    }
}
