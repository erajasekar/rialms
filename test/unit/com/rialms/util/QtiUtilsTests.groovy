package com.rialms.util

import grails.test.mixin.*
import grails.test.mixin.support.*

import org.custommonkey.xmlunit.XMLUnit

import static com.rialms.util.QtiUtils.*
import groovy.xml.MarkupBuilder

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

    /*void testConvertRubricBlockToNode() {
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
    }  */

    void testGetFeaturesFromItemXml() {
        String test = "testGetFeaturesFromItemXml";
        String msg = "${test} Failed ";

        def itemContent = {  builder ->
            createChoiceInteraction(builder,"1")
        }
        String xml = createItemXml('true', itemContent)
        File file = writeToTempFile(test, xml)
        getItemFeaturesAndValidate(file, ['adaptive','choice'], msg);
        assertTrue(msg, file.delete());

        itemContent = {  builder ->
            createChoiceInteraction(builder,"2")
        }
        xml = createItemXml('true', itemContent)
        file = writeToTempFile(test, xml)
        getItemFeaturesAndValidate(file, ['adaptive','choice multiple'], msg);
        assertTrue(msg, file.delete());

        itemContent = {  builder ->
            createChoiceInteraction(builder,"0")
        }
        xml = createItemXml('true', itemContent)
        file = writeToTempFile(test, xml)
        getItemFeaturesAndValidate(file, ['adaptive','choice multiple'], msg);
        assertTrue(msg, file.delete());

        itemContent = {  builder ->
            builder.itemBody(){
                builder.gapMatchInteraction(responseIdentifier: 'RESPONSE', shuffle: 'true') {
                    builder.prompt('What does it say?') {}
                    builder.gapText(identifier: 'ChoiceA', matchMax:"1", 'Winter')
                }
            }
        }
        xml = createItemXml('true', itemContent)
        file = writeToTempFile(test, xml)
        getItemFeaturesAndValidate(file, ['adaptive','gap match'], msg);
        assertTrue(msg, file.delete());

        itemContent = {  builder ->
            builder.itemBody(){
                builder.inlineChoiceInteraction(responseIdentifier: 'RESPONSE', shuffle: 'true') {
                    builder.inlineChoice(identifier: 'ChoiceA', 'Winter')
                }
            }
        }
        xml = createItemXml('false', itemContent)
        file = writeToTempFile(test, xml)
        getItemFeaturesAndValidate(file, ['inline choice'], msg);
        assertTrue(msg, file.delete());

        itemContent = {  builder ->
            builder.itemBody(){
                builder.p('some text'){
                    builder.textEntryInteraction(responseIdentifier: 'RESPONSE', expectedLength: '15')
                }
            }
        }
        xml = createItemXml('false', itemContent)
        file = writeToTempFile(test, xml)
        getItemFeaturesAndValidate(file, ['text entry'], msg);
        assertTrue(msg, file.delete());

        itemContent = {  builder ->
            builder.itemBody(){
                builder.orderInteraction(responseIdentifier: 'RESPONSE', shuffle: 'true') {
                    builder.prompt('What does it say?') {}
                    builder.simpleChoice(identifier: 'ChoiceA', 'You must stay with your luggage at all times.')
                }
            }
        }
        xml = createItemXml('false', itemContent)
        file = writeToTempFile(test, xml)
        getItemFeaturesAndValidate(file, ['order'], msg);
        assertTrue(msg, file.delete());

        itemContent = {  builder ->
            builder.itemBody(){
                builder.associateInteraction(responseIdentifier: 'RESPONSE', shuffle: 'true', maxAssociations:"3") {
                    builder.prompt('What does it say?') {}
                    builder.simpleAssociableChoice(identifier: 'ChoiceA', 'You must stay with your luggage at all times.')
                }
            }
        }
        xml = createItemXml('false', itemContent)
        file = writeToTempFile(test, xml)
        getItemFeaturesAndValidate(file, ['associate'], msg);
        assertTrue(msg, file.delete());

        itemContent = {  builder ->
            builder.itemBody(){
                builder.hottextInteraction(responseIdentifier: 'RESPONSE',  maxChoices: '15'){
                    hottext(identifier: 'C','at least')
                }
            }
        }
        xml = createItemXml('false', itemContent)
        file = writeToTempFile(test, xml)
        getItemFeaturesAndValidate(file, ['hottext'], msg);
        assertTrue(msg, file.delete());

        itemContent = {  builder ->
            builder.responseDeclaration(identifier: 'RESPONSE', cardinality:'multiple', baseType:'pair'){
                builder.mapping(defaultValue:'0'){
                    mapEntry(mapKey: 'A P',mappedValue: '2')
                }
            }
        }
        xml = createItemXml('false', itemContent)
        file = writeToTempFile(test, xml)
        getItemFeaturesAndValidate(file, ['partial scoring'], msg);
        assertTrue(msg, file.delete());

        itemContent = {  builder ->
            builder.templateProcessing(){
            }
        }
        xml = createItemXml('false', itemContent)
        file = writeToTempFile(test, xml)
        getItemFeaturesAndValidate(file, ['templated'], msg);
        assertTrue(msg, file.delete());

        itemContent = {  builder ->
            builder.itemBody(){
                builder.endAttemptInteraction(responseIdentifier: 'HINTREQUEST',  title: 'something'){}
                builder.endAttemptInteraction(responseIdentifier: 'SOLUTIONREQUEST',  title: 'something'){}
            }
        }
        xml = createItemXml('false', itemContent)
        file = writeToTempFile(test, xml)
        getItemFeaturesAndValidate(file, ['hint', 'solution'], msg);
        assertTrue(msg, file.delete());

        itemContent = {  builder ->
            builder.itemBody(){
                builder.endAttemptInteraction(responseIdentifier: 'HQUEST',  title: 'hint'){}
                builder.endAttemptInteraction(responseIdentifier: 'SQUEST',  title: 'solution'){}
            }
        }
        xml = createItemXml('false', itemContent)
        file = writeToTempFile(test, xml)
        getItemFeaturesAndValidate(file, ['hint','solution'], msg);
        assertTrue(msg, file.delete());

        itemContent = {  builder ->
            builder.itemBody(){
                builder.endAttemptInteraction(responseIdentifier: 'SOLNREQUEST',  title: 'something'){}
                builder.endAttemptInteraction(responseIdentifier: 'HREQUEST',  title: 'something'){}
            }
        }
        xml = createItemXml('false', itemContent)
        file = writeToTempFile(test, xml)
        getItemFeaturesAndValidate(file, [], msg);
        assertTrue(msg, file.delete());

        itemContent = {  builder ->
            builder.itemBody(){
                builder.choiceInteraction(responseIdentifier: 'RESPONSE', shuffle: 'true', maxChoices: '2') {
                    builder.prompt('What does it say?') {}
                    builder.simpleChoice(identifier: 'ChoiceA', 'You must stay with your luggage at all times.'){
                        feedbackInline(outcomeIdentifier:'FEEDBACK', identifier:'MSGHOO1', showHide:'show', 'Yes. Correct')
                    }
                }
            }
        }
        xml = createItemXml('false', itemContent)
        file = writeToTempFile(test, xml)
        getItemFeaturesAndValidate(file, ['feedback','choice multiple'], msg);
        assertTrue(msg, file.delete());

        itemContent = {  builder ->
            builder.itemBody(){
                builder.choiceInteraction(responseIdentifier: 'RESPONSE', shuffle: 'true', maxChoices: '2') {
                    builder.prompt('What does it say?') {}
                    builder.simpleChoice(identifier: 'ChoiceA', 'You must stay with your luggage at all times.'){
                        builder.feedbackInline(outcomeIdentifier:'FEEDBACK', identifier:'MSGHOO1', showHide:'show', 'Yes. Correct')
                    }
                }
            }
        }
        xml = createItemXml('false', itemContent)
        file = writeToTempFile(test, xml)
        getItemFeaturesAndValidate(file, ['feedback','choice multiple'], msg);
        assertTrue(msg, file.delete());

        itemContent = {  builder ->
            builder.itemBody(){
                builder.choiceInteraction(responseIdentifier: 'RESPONSE', shuffle: 'true', maxChoices: '2') {
                    builder.prompt('What does it say?') {}
                    builder.simpleChoice(identifier: 'ChoiceA', 'You must stay with your luggage at all times.'){}
                    builder.feedbackBlock(outcomeIdentifier:'FEEDBACK', identifier:'MSGHOO1', showHide:'show', 'Yes. Correct') {
                        builder.p('sometext'){}
                    }
                }
            }
        }
        xml = createItemXml('false', itemContent)
        file = writeToTempFile(test, xml)
        getItemFeaturesAndValidate(file, ['feedback','choice multiple'], msg);
        assertTrue(msg, file.delete());

        itemContent = {  builder ->
            builder.itemBody(){
                builder.choiceInteraction(responseIdentifier: 'RESPONSE', shuffle: 'true', maxChoices: '2') {
                    builder.prompt('What does it say?') {
                        builder.math{
                           builder.mrow(){
                               builder.mi('E')
                           }
                        }
                    }
                    builder.simpleChoice(identifier: 'ChoiceA', 'You must stay with your luggage at all times.'){}

                }
            }
        }
        xml = createItemXml('false', itemContent)
        file = writeToTempFile(test, xml)
        getItemFeaturesAndValidate(file, ['math','choice multiple'], msg);
        assertTrue(msg, file.delete());
    }

    void testGetFeaturesFromTestXml() {
        String test = "testGetFeaturesFromTestXml";
        String msg = "${test} Failed ";

        def testContent = {  builder ->
            builder.testPart(identifier: 'part1', navigationMode:'linear', submissionMode:'individual')
            builder.testPart(identifier: 'part2', navigationMode:'nonlinear', submissionMode:'simultaneous')
        }
        String xml = createTestXml(testContent)
        File file = writeToTempFile(test, xml)
        getTestFeaturesAndValidate(file, ['linear', 'nonlinear','individual', 'simultaneous'], msg);
        assertTrue(msg, file.delete());

        testContent = {  builder ->
            builder.testPart(identifier: 'part1', navigationMode:'linear', submissionMode:'individual'){
                builder.itemSessionControl('allowReview':true, 'allowSkipping':true, maxAttempts:'5');
            }
        }
        xml = createTestXml(testContent)
        file = writeToTempFile(test, xml)
        getTestFeaturesAndValidate(file, ['linear', 'individual', 'maxAttempts'], msg);
        assertTrue(msg, file.delete());

        testContent = {  builder ->
            builder.testPart(identifier: 'part1', navigationMode:'linear', submissionMode:'individual'){
                builder.itemSessionControl(){};
            }
        }
        xml = createTestXml(testContent)
        file = writeToTempFile(test, xml)
        getTestFeaturesAndValidate(file, ['linear', 'individual'], msg);
        assertTrue(msg, file.delete());

        testContent = {  builder ->
            builder.testPart(identifier: 'part1', navigationMode:'linear', submissionMode:'individual'){
                builder.itemSessionControl('allowReview':false, 'allowSkipping':false, maxAttempts: '1');
            }
        }
        xml = createTestXml(testContent)
        file = writeToTempFile(test, xml)
        getTestFeaturesAndValidate(file, ['linear', 'individual', 'disabled skipping', 'disabled review'], msg);
        assertTrue(msg, file.delete());

        testContent = {  builder ->
            builder.timeLimits(maxTime:'30'){}
            builder.testPart(identifier: 'part1', navigationMode:'linear', submissionMode:'individual')
        }
        xml = createTestXml(testContent)
        file = writeToTempFile(test, xml)
        getTestFeaturesAndValidate(file, ['linear', 'individual', 'timeout'], msg);
        assertTrue(msg, file.delete());

        testContent = {  builder ->
            builder.testPart(identifier: 'part1', navigationMode:'linear', submissionMode:'individual'){
                builder.timeLimits(maxTime:'30'){}
            }
        }
        xml = createTestXml(testContent)
        file = writeToTempFile(test, xml)
        getTestFeaturesAndValidate(file, ['linear', 'individual', 'timeout'], msg);
        assertTrue(msg, file.delete());

    }

    private void getItemFeaturesAndValidate(File inputFile, List<String> expectedFeatures, String msg) {
        List<String> features = getFeaturesFromItemXml(inputFile);
        assertEquals(msg, expectedFeatures, features);
    }

    private void getTestFeaturesAndValidate(File inputFile, List<String> expectedFeatures, String msg) {
        List<String> features = getFeaturesFromTestXml(inputFile);
        assertEquals(msg, expectedFeatures, features);
    }

    private void createChoiceInteraction(def builder,String maxChoices){
        builder.itemBody(){
            builder.choiceInteraction(responseIdentifier: 'RESPONSE', shuffle: 'true', maxChoices: maxChoices) {
                builder.prompt('What does it say?') {}
                builder.simpleChoice(identifier: 'ChoiceA', 'You must stay with your luggage at all times.')
            }
        }
    }
    private File writeToTempFile(String prefix, String content) {
        println "content ${content}"
        File inputFile = File.createTempFile(prefix, 'xml');
        inputFile.write(content);
        return inputFile;
    }

    private String createTestXml(Closure testContent){
        def writer = new StringWriter()
        def builder = new MarkupBuilder(writer)
        builder.assessmentItem(xmlns: 'http://www.imsglobal.org/xsd/imsqti_v2p1',
                'xmlns:xsi': 'http://www.w3.org/2001/XMLSchema-instance',
                'xsi:schemaLocation': 'http://www.imsglobal.org/xsd/imsqti_v2p1 http://www.imsglobal.org/xsd/imsqti_v2p1.xsd',
                identifier: 'identifier',
                title: 'Test title'){
            testContent(builder);
        }
        return writer.toString();
    }

    private String createItemXml(String adaptive, Closure itemContent) {
        def writer = new StringWriter()
        def builder = new MarkupBuilder(writer)
        builder.assessmentItem(xmlns: 'http://www.imsglobal.org/xsd/imsqti_v2p1',
                'xmlns:xsi': 'http://www.w3.org/2001/XMLSchema-instance',
                'xsi:schemaLocation': 'http://www.imsglobal.org/xsd/imsqti_v2p1 http://www.imsglobal.org/xsd/imsqti_v2p1.xsd',
                identifier: 'identifier',
                title: 'Test title',
                adaptive: adaptive,
                timeDependent: 'false') {
            itemContent(builder);
        }
        return writer.toString();
    }
}
