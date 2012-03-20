package com.rialms.taglib

import com.rialms.consts.Tag
import org.qtitools.qti.value.IdentifierValue
import com.rialms.util.CollectionUtils
import com.rialms.assessment.AssessmentItemInfo
import org.qtitools.qti.node.item.template.declaration.TemplateDeclaration
import org.qtitools.qti.value.BaseType
import com.rialms.util.QtiUtils

class QtiTagLib {
    static namespace = "qti";

    def img = {  attrs ->

        String dir = getRequiredAttribute(attrs, 'dir', 'img');
        String file = getRequiredAttribute(attrs, 'file', 'img');
        String fullPath = dir + file;

        int i = fullPath.lastIndexOf('/');
        Map fieldAttributes = [dir: fullPath.substring(0, i), file: fullPath.substring(i + 1)];

        def tagBody = {
            g.img(fieldAttributes);
        }
        renderTag(fieldAttributes, tagBody);
    }

    def textEntryInteraction = {  attrs ->

        Node xmlNode = getRequiredAttribute(attrs, 'xmlNode', 'textEntryInteraction');
        AssessmentItemInfo assessmentItemInfo = getRequiredAttribute(attrs, 'assessmentItemInfo', 'textEntryInteraction');
        Map responseValues = assessmentItemInfo.responseValues;

        String id = xmlNode.'@responseIdentifier';
        String maxlength = xmlNode.'@expectedLength';

        Map fieldAttributes = [name: id];

        if (maxlength) {
            fieldAttributes.maxlength = fieldAttributes.size = maxlength;
        }

        def value = responseValues[id];

        if (value) {
            fieldAttributes['value'] = value;
        }

        //TODO
        log.info("textEntryInteraction Field Attributes ${fieldAttributes}");

        def tagBody = {
            g.textField(fieldAttributes);
        }
        renderTag(attrs, tagBody);
    }

    def printedVariable = { attrs ->

        Map xmlAttributes = getRequiredAttribute(attrs, 'xmlAttributes', 'printedVariable');
        AssessmentItemInfo assessmentItemInfo = getRequiredAttribute(attrs, 'assessmentItemInfo', 'printedVariable');

        Map templateValues = assessmentItemInfo.templateValues;
        Map outcomeValues = assessmentItemInfo.outcomeValues;

        String id = xmlAttributes.identifier;
        String templateValue = templateValues[id];
        String outcomeValue = outcomeValues[id];
        String format = xmlAttributes.format;

        if (templateValue) {
            if (format) {
                out << " ${QtiUtils.formatVariable(assessmentItemInfo.getTemplateDeclarationForIdentifier(id), format, templateValue)} ";
            } else {
                out << " ${templateValue} ";
            }
        } else if (outcomeValue) {
            if (format) {
                out << " ${QtiUtils.formatVariable(assessmentItemInfo.getOutcomeDeclarationForIdentifier(id), format, outcomeValue)} ";
            } else {
                out << " ${outcomeValue} ";
            }
        }
    }

    def choiceInteraction = {  attrs ->

        Node xmlNode = getRequiredAttribute(attrs, 'xmlNode', 'choiceInteraction');
        AssessmentItemInfo assessmentItemInfo = getRequiredAttribute(attrs, 'assessmentItemInfo', 'choiceInteraction');

        Map responseValues = assessmentItemInfo.responseValues
        Map outcome = assessmentItemInfo.outcomeValues;

        String dataPath = getRequiredAttribute(attrs, 'dataPath', 'choiceInteraction');

        String id = xmlNode.'@responseIdentifier';
        String maxChoices = xmlNode.attribute("maxChoices");
        boolean shuffle = xmlNode.attribute("shuffle")?.toBoolean();

        String prompt;
        List values = [];

        Map fixedChoices = [:];    //<position, choice >
        List shuffledChoices = [];
        List allChoices = [];
        int position = 0;
        xmlNode.children().each {child ->
            Tag tag = Tag.valueOf(child.name());
            switch (tag) {
                case Tag.prompt: prompt = child.text();
                    break;

                case Tag.simpleChoice:
                    values << child.attribute('identifier');
                    if (child.attribute("fixed")?.toBoolean()) {
                        fixedChoices[position] = child;
                    } else {
                        if (shuffle) {
                            shuffledChoices << child;
                        } else {
                            fixedChoices[position] = child;
                        }
                    }
                    position++;
                    break;
            }
        }

        if (shuffle) {
            allChoices = CollectionUtils.shuffleWithFixedPositions(shuffledChoices, fixedChoices);
            values = allChoices.collect {
                if (Tag.valueOf(it.name()) == Tag.simpleChoice) {
                    it.attribute('identifier');
                }
            }
        } else {
            allChoices = CollectionUtils.orderValuesByPosition(fixedChoices);
        }

        Map fieldAttributes = [name: id, values: values, labels: allChoices]

        def value = responseValues[id];

        if (value) {
            fieldAttributes['value'] = value;
        }
        //TODO
        log.info("choiceInteraction Field Attributes ${fieldAttributes}");

        if (maxChoices.toInteger() == 1) {
            def tagBody = {
                out << """<p> ${prompt} </p>""";
                g.radioGroup(fieldAttributes) {
                    out << "<p> ${it.radio}";
                    out << g.render(template: '/renderer/renderItemBody', model: [node: it.label, outcome: outcome, dataPath: dataPath]);
                    out << " </p> ";
                };

            }
            renderTag(attrs, tagBody);
        } else {
            out << """<p> ${prompt} </p>""";

            values.eachWithIndex {v, i ->
                boolean checked = (value && value.split(',').contains(v));
                out << "<p>";
                out << g.checkBox(name: id, value: v, checked: checked);
                out << g.render(template: '/renderer/renderItemBody', model: [node: allChoices[i], outcome: outcome, dataPath: dataPath]);
                out << " </p> ";
            }
        }


    }

    def inlineChoiceInteraction = {  attrs ->

        Node xmlNode = getRequiredAttribute(attrs, 'xmlNode', 'inlineChoiceInteraction');
        AssessmentItemInfo assessmentItemInfo = getRequiredAttribute(attrs, 'assessmentItemInfo', 'inlineChoiceInteraction');

        Map responseValues = assessmentItemInfo.responseValues;
        Map outcome = assessmentItemInfo.outcomeValues;

        String id = xmlNode.'@responseIdentifier';
        boolean shuffle = xmlNode.attribute("shuffle")?.toBoolean();

        Map fixedChoices = [:];    //<position, choice >
        List shuffledChoices = [];
        List allChoices = [];
        List from = [];
        List keys = [];
        int position = 0;
        xmlNode.children().each {child ->
            if (child.attribute("fixed")?.toBoolean()) {
                fixedChoices[position] = child;
            }
            else {
                if (shuffle) {
                    shuffledChoices << child;
                } else {
                    fixedChoices[position] = child;
                }
            }
            position++;
            keys << child.'@identifier';
            from << child.text();
        }

        if (shuffle) {
            allChoices = CollectionUtils.shuffleWithFixedPositions(shuffledChoices, fixedChoices);
            keys = allChoices.collect { it.'@identifier';}
            from = allChoices.collect { it.text();}
        }

        Map fieldAttributes = [name: id, keys: keys, from: from];
        def value = responseValues[id];

        if (value) {
            fieldAttributes['value'] = value;
        }

        //TODO
        log.info("inlineChoiceInteraction ${fieldAttributes}")

        def tagBody = {
            g.select(fieldAttributes);
        }
        renderTag(attrs, tagBody);

    }

    private void renderTag(Map fieldAttributes, Closure tagBody) {
        out << """  ${tagBody()} """;
    }

    protected getAttribute(attrs, String name, String tagName, boolean isRequired = false) {
        if (isRequired && !attrs.containsKey(name)) {
            throwTagError("Tag [$tagName] is missing required attribute [$name]")
        }
        attrs.remove name
    }

    protected getRequiredAttribute(attrs, String name, String tagName) {
        return getAttribute(attrs, name, tagName, true);
    }

}