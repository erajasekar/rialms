/**
 * Created by IntelliJ IDEA.
 * User: relango
 * Date: 5/09/12
 * Time: 11:18 PM
 * To change this template use File | Settings | File Templates.
 */
package com.rialms.assessment.test

import static org.junit.Assert.*

import grails.test.mixin.*
import grails.test.mixin.support.*
import org.junit.*
import org.springframework.core.io.ClassPathResource

import static org.junit.Assert.assertNull

import com.rialms.assessment.item.AssessmentItemInfo
import com.rialms.consts.AssessmentItemStatus
import static com.rialms.assessment.test.SectionPartStatus.Position.*
import static com.rialms.consts.AssessmentItemStatus.*

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
class AssessmentTestControllerTests {

    public static final String TEST_FILE_NL_INDIVIDUAL = 'NonLinearIndividual.xml';
    public static final String TEST_FILE_L_INDIVIDUAL = 'LinearIndividual.xml';
    public static final String TEST_FILE_SIMULTANEOUS = 'Simultaneous.xml';
    public static final String TEST_FILE_DEEP_NESTED = 'deep-nested.xml';
    public static final String TEST_FILE_DEEP_NESTED_DISABLED_REVIEW = 'deep-nested-disabled-review.xml';
    public static final String DATA_PATH = 'data/AssessmentTestControllerTests';

    void testGetItemByIdentifier() {
        String msg = "testGetItemByIdentifier Failed ";
        AssessmentTestController test = createTestController(TEST_FILE_NL_INDIVIDUAL);

        assertEquals("${msg} on check find next item by identifier", 'math5', test.getItemByIdentifier('math5', true)?.identifier);
        assertEquals("${msg} on check find previous item by identifier", 'math1', test.getItemByIdentifier('math1', false)?.identifier);

        //should find across testParts
        assertEquals("${msg} on check find next item by identifier for different test part", 'math10', test.getItemByIdentifier('math10', true)?.identifier);

        //can'f find across testsParts, so should be null
        assertNull("${msg} on check find previous item by identifier for different test part", test.getItemByIdentifier('math1', false));

        assertNull("${msg} on check find next item by identifier for invalid identifier", test.getItemByIdentifier('mathx', true));
        assertNull("${msg} on check find previous item by identifier for invalid identifier", test.getItemByIdentifier('mathx', false));

    }

    void testGetCurrentItemInfo() {
        String msg = "testGetCurrentItemInfo Failed ";
        AssessmentTestController test = createTestController(TEST_FILE_NL_INDIVIDUAL);

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

    void testIsTestComplete() {
        String msg = "testIsTestComplete Failed ";
        doTestIsComplete(msg, TEST_FILE_NL_INDIVIDUAL);
        doTestIsComplete(msg, TEST_FILE_SIMULTANEOUS);
    }

    void testGetCurrentTestPartStatus() {
        String msg = "testGetItemByIdentifier Failed ";
        doTestGetCurrentTestPartStatusForNested(msg, TEST_FILE_DEEP_NESTED, true);
        doTestGetCurrentTestPartStatusForNested(msg, TEST_FILE_DEEP_NESTED_DISABLED_REVIEW, false);
        AssessmentTestController test = createTestController(TEST_FILE_L_INDIVIDUAL);
        test.getNextItem(true);
        2.times {
            test.getCurrentItemInfo();
            test.skipCurrentItem();
            test.getNextItem(true);
        }

        String sectionA = 'sectionA';
        Map expected = [(sectionA): [new SectionPartStatus('math1', sectionA, SKIPPED, BEFORE, true), new SectionPartStatus('math2', sectionA, SKIPPED, BEFORE, true)]]

        log.info("--------- Expected CurrentTestPartStatus ------------------")
        expected.each { k, v ->
            log.info("${k} ===> ${v}");;
        }
        log.info("------- Actual CurrentTestPartStatus ----------------------")
        test.getCurrentTestPartStatus().each { k, v ->
            log.info("${k} ===> ${v}");
        }

        assertEquals("${msg} on linear invidual items check", expected, test.getCurrentTestPartStatus());
    }

    private void doTestGetCurrentTestPartStatusForNested(String msg, String inputFile, boolean allowReview) {
        AssessmentTestController test = createTestController(inputFile);
        test.getNextItem(true);
        List<String> presentedIds = [test.currentItemRef.identifier];
        while (!test.hasNoMoreItemsInCurrentTestPart()) {
            test.getCurrentItemInfo();
            test.getNextItem(true);

            Map testPartStatus = test.getCurrentTestPartStatus();

            log.info("Presented ids ==> ${presentedIds}")
            Map<String, SectionPartStatus> testPartStatusById = testPartStatus.values().flatten().collectEntries {[it.identifier, it]};
            String currentId = test.currentItemRef.identifier;
            testPartStatusById.each { id, partStatus ->
                if (presentedIds.contains(id)) {
                    assertEquals("${msg} on status check", PRESENTED, partStatus.status)
                    assertEquals("${msg} on position check", BEFORE, partStatus.position)
                } else {
                    assertEquals("${msg} on status check", NOT_PRESENTED, partStatus.status)
                    if (id == currentId) {
                        assertEquals("${msg} on position check", CURRENT, partStatus.position)
                    } else {
                        assertEquals("${msg} on position check", AFTER, partStatus.position)
                    }
                }
            }
            presentedIds << currentId;

        }

        String A = "A";
        String B = "B";
        String C_D = "C${SectionPartStatus.PARENT_SECTION_DELIMITER}D";
        String E = "E";
        String F_G_H = "F${SectionPartStatus.PARENT_SECTION_DELIMITER}G${SectionPartStatus.PARENT_SECTION_DELIMITER}H";

        Map expected = [(A): [new SectionPartStatus('math1', A, PRESENTED, BEFORE, allowReview), new SectionPartStatus('math2', A, PRESENTED, BEFORE, allowReview)],
                (B): [new SectionPartStatus('math3', B, PRESENTED, BEFORE, allowReview), new SectionPartStatus('math4', B, PRESENTED, BEFORE, allowReview)],
                (C_D): [new SectionPartStatus('math5', C_D, PRESENTED, BEFORE, allowReview), new SectionPartStatus('math6', C_D, PRESENTED, BEFORE, allowReview)],
                (E): [new SectionPartStatus('math7', E, PRESENTED, BEFORE, allowReview)],
                (F_G_H): [new SectionPartStatus('math8', F_G_H, PRESENTED, BEFORE, allowReview), new SectionPartStatus('math9', F_G_H, NOT_PRESENTED, CURRENT, false)]
        ]

        Map actual = test.getCurrentTestPartStatus();
        log.info("------- Actual CurrentTestPartStatus ----------------------")
        actual.each { k, v ->
            log.info("${k} ===> ${v}");
        }

        log.info("--------- Expected CurrentTestPartStatus ------------------")
        expected.each { k, v ->
            log.info("${k} ===> ${v}");
        }

        assertEquals("${msg} on nested section items check", expected,actual);

        //test filtering
        assertEquals("${msg} on nested section items filter by presented check", filterTestPartStatus(expected,AssessmentItemStatus.PRESENTED), test.getCurrentTestPartStatus(AssessmentItemStatus.PRESENTED));
        assertEquals("${msg} on nested section items filter by not presented check", filterTestPartStatus(expected,AssessmentItemStatus.NOT_PRESENTED), test.getCurrentTestPartStatus(AssessmentItemStatus.NOT_PRESENTED));
    }

    private static Map filterTestPartStatus(Map testPartStatus, AssessmentItemStatus filterByStatus){
        Map filteredStatus = [:];
        testPartStatus.each{ k, v ->
            List filteredValues = v.findAll{it.status == filterByStatus};
            if (filteredValues){
                filteredStatus[k] = filteredValues.flatten();
            }
        }
        return filteredStatus;
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
        File inputFile = new ClassPathResource("${DATA_PATH}${File.separator}${fileName}").getFile();
        AssessmentTestController test = new AssessmentTestController(inputFile, DATA_PATH);
        return test;
    }
}
