package com.rialms.util

import static org.junit.Assert.*

import grails.test.mixin.*
import grails.test.mixin.support.*
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import com.rialms.assessment.test.AssessmentTestController
import org.qtitools.qti.node.content.variable.RubricBlock
import groovy.xml.XmlUtil
import org.custommonkey.xmlunit.XMLUnit
import org.custommonkey.xmlunit.Diff

/**
 * Created by IntelliJ IDEA.
 * User: relango
 * Date: 3/27/12
 * Time: 1:54 PM
 * To change this template use File | Settings | File Templates.
 */
@TestMixin(GrailsUnitTestMixin)
class QtiUtilsTests {

    void setUp() {
        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setNormalizeWhitespace(true);
        XMLUnit.setIgnoreComments(true);
    }

    void testConvertRubricBlockToNode() {
        String msg = "testConvertRubricBlockToNode Failed ";
        File inputFile = new ClassPathResource("data/TestConvertRubricBlockToNodeInputData.xml").getFile()
        File expectedFile = new ClassPathResource("data/TestConvertRubricBlockToNodeExpectedData.xml").getFile()

        assertTrue(msg, inputFile.exists());
        assertTrue(msg, expectedFile.exists());

        AssessmentTestController controller = new AssessmentTestController(inputFile, null);
        controller.getNextItem(true);
        List<List<RubricBlock>> blocks = controller.getRubricBlocks();

        Node actual = QtiUtils.convertRubricToNode(blocks);
        def xmlDiff = new Diff(expectedFile.text, XmlUtil.serialize(actual));
        assertTrue(msg + xmlDiff.toString(), xmlDiff.similar());
        assertNull(msg, QtiUtils.convertRubricToNode(null));
        assertNull(msg, QtiUtils.convertRubricToNode([[]]));

    }

    void testConvertStringArrayToNode() {
        String msg = "testConvertStringArrayToNode Failed ";
        Node actual = QtiUtils.convertStringArrayToNode(['title1', ' title2 ', '   title3']);
        String expected = """<?xml version="1.0" encoding="UTF-8"?>
<root xmlns='http://www.imsglobal.org/xsd/imsqti_v2p1'>
<value>title1</value>
<value> title2 </value>
<value>   title3</value>
</root>"""
        def xmlDiff = new Diff(expected, XmlUtil.serialize(actual));
        assertTrue(msg + xmlDiff.toString(), xmlDiff.similar());

        assertNull(msg, QtiUtils.convertStringArrayToNode(null));
        assertNull(msg, QtiUtils.convertStringArrayToNode([]));
    }

    void testApplyTemplateValuesInMathML() {

        String msg = "testApplyTemplateValuesInMathML failed"
        String input = """
                    <m:math xmlns:m="http://www.w3.org/1998/Math/MathML">
                        <m:mfrac>
                            <m:mi>num</m:mi>
                            <m:mi>den</m:mi>
                        </m:mfrac>
                    </m:math>
                            """;
        Node xmlNode = new XmlParser().parseText(input);
        Map<String, String> templateValues = [num: '1', den: '5'];

        Node actualResult = QtiUtils.applyTemplateValuesInMathML(xmlNode, templateValues);

        String expectedResult = """ <m:math xmlns:m="http://www.w3.org/1998/Math/MathML">
          <m:mfrac>
            <m:mn>1</m:mn>
            <m:mn>5</m:mn>
          </m:mfrac>
        </m:math> """;

        def xmlDiff = new Diff(expectedResult, XmlUtil.serialize(actualResult));
        assertTrue(msg + xmlDiff.toString(), xmlDiff.similar());
    }
}
