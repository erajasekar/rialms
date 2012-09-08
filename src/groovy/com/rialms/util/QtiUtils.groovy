package com.rialms.util

import groovy.xml.MarkupBuilder
import org.qtitools.qti.node.content.variable.RubricBlock
import org.qtitools.qti.node.shared.VariableDeclaration
import org.qtitools.qti.node.test.TestFeedback
import org.qtitools.qti.value.BaseType
import org.qtitools.qti.value.MultipleValue
import org.qtitools.qti.value.Value
import org.qtitools.qti.value.ListValue
import com.rialms.consts.Tag
import com.rialms.consts.Constants

/**
 * Created by IntelliJ IDEA.
 * User: relango
 * Date: 3/19/12
 * Time: 11:24 PM
 * To change this template use File | Settings | File Templates.
 */
class QtiUtils {

    private static String ns = 'http://www.imsglobal.org/xsd/imsqti_v2p1';

    public static Map<String, List<String>> convertToRespValues(Map params, List identifiers) {
        Map<String, List<String>> map = new HashMap<String, List<String>>()

        identifiers.each {i ->
            List<String> values = [];
            def respValue = params[i];

            if (respValue) {
                if (respValue instanceof String && !respValue.isEmpty()) {
                    values << respValue;
                } else {
                    respValue.each { if (!it.isEmpty()) {values.add(it)}}
                }
                map.put(i, values)
            }
        }
        return map
    }

    public static Map<String, String> convertQTITypesToParams(Map<String, Value> values) {

        Map<String, String> params = [:];

        values?.each { k, v ->
            String value;
            if (!(v instanceof org.qtitools.qti.value.NullValue)) {

                if (v instanceof ListValue) {
                    value = v.getAll().join(',');
                }
                else {
                    value = v.toString();
                }
                params[k] = value;
            }
        }
        return params;
    }

    public static Map<String, List<String>> convertRespValuesToStringMap(Map<String, Value> respValues) {

        Map<String, String> params = [:];

        respValues?.each { k, v ->
            List<String> values = [];
            if (!(v instanceof org.qtitools.qti.value.NullValue)) {
                if (v instanceof ListValue) {
                    values = v.collect {it.toString()};
                }
                else {
                    values << v.toString();
                }
                params[k] = values;
            }
        }
        return params;
    }

    public static Map<String, String> convertMultipleResponseValuesToMap(List<String> responseValues) {
        Map<String, String> result = responseValues.collectEntries {responseValue ->
            List values = responseValue.split(' ');
            return [(values[1]): values[0]];
        }
        result;
    }

    public static String formatVariable(VariableDeclaration variableDeclaration, String format, Object value) {
        BaseType type = variableDeclaration.getBaseType();
        if (type == BaseType.INTEGER) {
            String.format(format, value.toInteger());
        } else if (type == BaseType.FLOAT) {
            String.format(format, value.toFloat());
        } else {
            return value.toString();
        }
    }

    public static VariableDeclaration findVariableDeclarationByIdentifier(List<VariableDeclaration> variableDeclarations, String identifier) {
        return variableDeclarations.find {VariableDeclaration it ->
            it.identifier.toString() == identifier
        }
    }

    public static Node convertRubricToNode(List<List<RubricBlock>> values) {
        if (values == null) {
            return null;
        }
        def writer = new StringWriter()
        MarkupBuilder builder = new MarkupBuilder(writer)
        int count;
        builder.root(xmlns: ns) {
            count = appendSectionToBuilder(builder, values)
        }

        if (count == 0) {
            return null;
        }
        return new XmlParser().parseText(writer.toString());
    }

    private static int appendSectionToBuilder(builder, values) {
        int count = 0;
        values.each { section ->
            builder.section() {
                count += appendNodeToBuilder(builder, section);
            }
        }
        return count;
    }

    private static int appendNodeToBuilder(MarkupBuilder builder, nodes) {
        int count = 0;
        nodes.each { node ->
            count++;
            builder.mkp.yieldUnescaped(node.toXmlString())
        }
        return count;
    }


    public static Node convertStringArrayToNode(List<String> values) {
        if (values == null || values.isEmpty()) {
            return null;
        }

        StringWriter writer = new StringWriter()
        def builder = new MarkupBuilder(writer)

        builder.root(xmlns: ns) {
            appendStringToBuilder(builder, values)
        }
        return new XmlParser().parseText(writer.toString());
    }

    private static void appendStringToBuilder(builder, values) {
        values.each { value ->
            builder.value(value);
        }
    }

    public static Node convertFeedbackToNode(List<TestFeedback> values) {
        if (values == null || values.isEmpty()) {
            return null;
        }
        def writer = new StringWriter()
        def builder = new MarkupBuilder(writer)
        builder.root(xmlns: ns) {
            appendNodeToBuilder(builder, values)
        }
        return new XmlParser().parseText(writer.toString());
    }

    public static Node applyTemplateValuesInMathML(Node xmlNode, Map<String, String> templateValues) {

        xmlNode.depthFirst().each { Node node ->
            if (node.name().localPart == 'mi') {
                String identifier = node.text();
                String identifierValue = templateValues[identifier];
                if (identifierValue) {
                    node.name().localPart = 'mn';
                    node.setValue(identifierValue);
                }
            }
        }
        return xmlNode;
    }

    public static String getTitleFromXml(File input) {
        return new XmlSlurper().parse(input).'@title';
    }

    public static Node findParentByTag(Node start, Tag tag){
        if (Tag.valueOf(start.name()) == tag){
            return start;
        }
        return findParentByTag(start.parent(),tag);
    }

    public static List<String> getFeaturesFromItemXml(File input){
        List<String> featureNames = [];
        def itemXml = new XmlSlurper().parse(input);
        if (itemXml.'@adaptive'?.toBoolean()){
            featureNames << 'adaptive'
        }

        if (itemXml.responseDeclaration.children().find{it.name() == 'mapping'}){
            featureNames << 'partial scoring'
        }

        if (itemXml.children().find{it.name() == 'templateProcessing'}){
            featureNames << 'templated'
        }

        if (itemXml.itemBody.depthFirst().find{it.name() == 'math'}){
            featureNames << 'math'
        }

        def interactions = itemXml.itemBody.depthFirst().findAll {it.name().endsWith(Constants.Interaction)}
        def interactionFeatures = interactions.collect{
            String interactionName = it.name();
            String featureName = interactionName - Constants.Interaction
            if (interactionName == Tag.choiceInteraction.name() && it.'@maxChoices'.toInteger() != 1){
                featureName = featureName + ' multiple'
            }else if (interactionName == Tag.gapMatchInteraction.name() || interactionName == Tag.inlineChoiceInteraction.name() || interactionName == Tag.textEntryInteraction.name()){
                featureName = StringUtils.convertCamelCaseToWords(featureName);
            }else if (interactionName == Tag.endAttemptInteraction.name()){
                String responseIdentifier = it.'@responseIdentifier';
                String title = it.'@title';
                if (responseIdentifier.toLowerCase().contains('hint') || title.contains('hint')){
                    featureName = 'hint'
                }
                else if (responseIdentifier.toLowerCase().contains('solution') || title.contains('solution')){
                    featureName = 'solution'
                }else{
                    featureName = ''
                }

            }
            return featureName
        }

        if (itemXml.itemBody.depthFirst().find{it.name().contains('feedback')}){
            featureNames << 'feedback'
        }

        if (!interactionFeatures.isEmpty()){
            interactionFeatures.each {
               if(!it.isEmpty()){
                   featureNames << it;
               }
            }
        }
        return featureNames.unique();

    }

    public static List<String> getFeaturesFromTestXml(File input){
        List<String> featureNames = [];
        def testXml = new XmlSlurper().parse(input);

        def testParts = testXml.children().findAll {it.name() == 'testPart'};
        if (testParts.size() > 1){
            featureNames << 'multiple parts'
        }
        featureNames << testParts.collect(){it.'@navigationMode'.toString()}
        featureNames << testParts.collect(){it.'@submissionMode'.toString()}

        def isc = testXml.depthFirst().findAll {it.name() == 'itemSessionControl'};
        isc.each{
            String allowSkipping = it.'@allowSkipping'.toString();
            if (!allowSkipping.isEmpty() && allowSkipping.toBoolean() == false){
                featureNames << 'disabled skipping'
            }
            String allowReview = it.'@allowReview'.toString();
            if (!allowReview.isEmpty() && allowReview.toBoolean() == false){
                featureNames << 'disabled review'
            }
            String maxAttempts = it.'@maxAttempts'.toString();
            if (!maxAttempts.isEmpty() && maxAttempts?.toInteger() > 1){
                featureNames << 'max attempts'
            }
        }

        if (testXml.depthFirst().find{it.name() == 'timeLimits'}){
            featureNames << 'timeout';
        }

        if (testXml.depthFirst().find{ it.name() == 'assessmentSection' && it.parent().name() == 'assessmentSection'}){
            featureNames << 'nested';
        }

        if (testXml.depthFirst().find{it.name() == 'preCondition' || it.name() == 'branchRule'}){
            featureNames << 'branch';
        }

        def testVariables = testXml.depthFirst().findAll {it.name() == 'testVariables'};
        if (testVariables.find{testVariable -> testVariable.attributes().containsKey('includeCategory') ||  testVariable.attributes().containsKey('excludeCategory')}){
            featureNames << 'score using item categories'
        }

        def itemRefs = testXml.depthFirst().findAll {it.name() == 'assessmentItemRef'};
        for (def itemRef in itemRefs){
            if (itemRef.children().find{
                it.name() == 'weight'
            }){
                featureNames << 'weight item outcomes'
            }
        }

        if (testXml.depthFirst().find{it.name() == 'exitTest'}){
            featureNames << 'early termination';
        }

        def ordering = testXml.depthFirst().findAll {it.name() == 'ordering'};
        if (ordering.find{it.'@shuffle' == 'true'}){
            featureNames << 'randomize order'
        }

        return featureNames.flatten().unique();
    }
}

