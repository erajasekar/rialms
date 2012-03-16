package com.rialms.taglib

import com.rialms.consts.Tag
import org.qtitools.qti.value.IdentifierValue

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

        Map xmlAttributes = getRequiredAttribute(attrs, 'xmlAttributes', 'textEntryInteraction');
        Map responseValues = getRequiredAttribute(attrs, 'responseValues', 'textEntryInteraction');
        String id = xmlAttributes.responseIdentifier;

        Map fieldAttributes = [name: id]

        def value = responseValues[id];

        if (!(value instanceof org.qtitools.qti.value.NullValue)) {
            fieldAttributes['value'] = value;
        }

        def tagBody = {
            g.textField(fieldAttributes);
        }
        renderTag(attrs, tagBody);
    }

    def choiceInteraction = {  attrs ->

        Node xmlNode = getRequiredAttribute(attrs, 'xmlNode', 'choiceInteraction');
        Map responseValues = getRequiredAttribute(attrs, 'responseValues', 'choiceInteraction');
        Map outcome = getRequiredAttribute(attrs, 'outcome', 'choiceInteraction');
        String exercisePath = getRequiredAttribute(attrs, 'exercisePath', 'choiceInteraction');

        String id = xmlNode.attribute('responseIdentifier');
        String maxChoices = xmlNode.attribute("maxChoices");

        String prompt;
        List labels = [];
        List values = [];
        xmlNode.children().each {child ->
            Tag tag = Tag.valueOf(child.name());
            println "tag ${tag}"
            switch (tag) {
                case Tag.prompt: prompt = child.text();
                    break;

                case Tag.simpleChoice:
                    labels << child;
                    values << child.attribute('identifier');
                    break;
            }
        }


        Map fieldAttributes = [name: id, values: values, labels: labels]

        def value = responseValues[id];

        if (!(value instanceof org.qtitools.qti.value.NullValue)) {
            fieldAttributes['value'] = value;
        }

        log.info("choiceInteraction Field Attributes ${fieldAttributes}");


        if (maxChoices.toInteger() == 1) {
            def tagBody = {
                out << """<p> ${prompt} </p>""";
                /* xmlNode.children().each {child ->
                     if (Tag.simpleChoice.equals(child.name())){
    
                         String radioValue = child.attribute('identifier');
                         println "radioValue ${radioValue} myvalue ${value} ${radioValue.equals(value)}";
                         if (radioValue == value) {
                             println "here.."
                             out << """<p><input type='radio' name=${id} value=${radioValue} checked='checked' />""";
                         }else{
                             out << """<p><input type='radio' name=${id} value=${radioValue} />""";
                         }
    
                         //TODO fix exercise path
                         out << g.render(template: '/renderer/renderItemBody', model: [node:child, outcome:outcome, exercisePath:'/content/qti']);
                         out << "</p>"
    
                     }
                }*/

                g.radioGroup(fieldAttributes) {
                    out << "<p> ${it.radio}";
                    out << g.render(template: '/renderer/renderItemBody', model: [node: it.label, outcome: outcome, exercisePath: exercisePath]);
                    out << " </p> ";
                };

            }
            renderTag(attrs, tagBody);
        } else {
            out << """<p> ${prompt} </p>""";

            values.eachWithIndex {v, i ->
                boolean checked = (value && !(value instanceof org.qtitools.qti.value.NullValue) && value.getAll().contains(new IdentifierValue(v)));
                out << "<p>";
                out << g.checkBox(name: id, value: v, checked: checked);
                out << g.render(template: '/renderer/renderItemBody', model: [node: labels[i], outcome: outcome, exercisePath: exercisePath]);
                out << " </p> ";
            }
        }


    }



    private void renderTag(Map fieldAttributes, Closure tagBody) {
        out << """  <div> ${tagBody()} </div>
           """;
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
