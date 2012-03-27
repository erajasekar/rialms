package com.rialms.util

import org.qtitools.qti.value.Value
import org.qtitools.qti.value.MultipleValue
import org.qtitools.qti.value.BaseType
import org.qtitools.qti.node.shared.VariableDeclaration
import org.qtitools.qti.node.item.template.declaration.TemplateDeclaration
import org.qtitools.qti.node.content.variable.RubricBlock
import org.w3c.dom.Element
import groovy.xml.XmlUtil
import org.qtitools.qti.node.test.TestFeedback

/**
 * Created by IntelliJ IDEA.
 * User: relango
 * Date: 3/19/12
 * Time: 11:24 PM
 * To change this template use File | Settings | File Templates.
 */
class QtiUtils {

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

    public static Map<String, String> convertQTITypesToParams(Map<String, Value> outcome) {

        Map<String, String> params = [:];

        outcome.each { k, v ->
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

        Node result = new Node(null, "root");
        for (List<RubricBlock> section: values) {
            Node sectionNode = result.appendNode("section")
            for (RubricBlock block: section) {
                sectionNode.append(new XmlParser().parseText(block.toXmlString()));
            }
        }
        return result;
    }

    public static Node convertStringArrayToNode(List<String> values) {
        Node result = new Node(null, "root");
        values.each { value ->
            result.appendNode("value", null, value);
        }
        return result;
    }

    public static Node convertFeedbackToNode(List<TestFeedback> values) {
        Node result = new Node(null, "root");
        values.each {TestFeedback value ->
            result.append(new XmlParser().parseText(value.toXmlString()));
        }
        return result;
    }
}
