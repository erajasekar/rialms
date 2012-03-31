package com.rialms.util

import groovy.xml.MarkupBuilder
import org.qtitools.qti.node.content.variable.RubricBlock
import org.qtitools.qti.node.shared.VariableDeclaration
import org.qtitools.qti.node.test.TestFeedback
import org.qtitools.qti.value.BaseType
import org.qtitools.qti.value.MultipleValue
import org.qtitools.qti.value.Value

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
                if (respValue instanceof String) {
                    values << respValue;
                } else {
                    respValue.each {values.add(it)}
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
                if (v instanceof MultipleValue) {
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
                if (v instanceof MultipleValue) {
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
                    node.setValue(identifierValue);
                }
            }
        }
        return xmlNode;
    }
}
