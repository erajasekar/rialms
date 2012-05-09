package com.rialms.assessment.test

import static org.junit.Assert.*

import grails.test.mixin.*
import grails.test.mixin.support.*
import org.junit.*
import org.springframework.core.io.ClassPathResource

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
class AssessmentTestControllerTests {

    public static final String TEST_FILE_INDIVIDUAL = 'Individual.xml';

    void testGetItemByIdentifier() {
        AssessmentTestController test = createTestController(TEST_FILE_INDIVIDUAL);
        test.getItemByIdentifier('math5', true);
        println test.currentItemIdentifier;
    }

    private AssessmentTestController createTestController(String fileName) {
        File inputFile = new ClassPathResource("data/AssessmentTestControllerTests/Individual.xml").getFile();
        AssessmentTestController test = new AssessmentTestController(inputFile, "data/AssessmentTestControllerTests");
    }
}
