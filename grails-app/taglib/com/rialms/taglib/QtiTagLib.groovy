package com.rialms.taglib

import com.rialms.consts.Tag

import com.rialms.util.CollectionUtils
import com.rialms.assessment.item.AssessmentItemInfo

import com.rialms.util.QtiUtils
import com.rialms.assessment.render.HiddenElement
import com.rialms.assessment.test.SectionPartStatus
import com.rialms.consts.Constants

class QtiTagLib {
    static namespace = "qti";


    def img = {  attrs ->

        String tag = "img";
        String dir = getRequiredAttribute(attrs, 'assessmentItemInfo', tag).dataPath;
        Node xmlNode = getRequiredAttribute(attrs, 'xmlNode', tag);
        String file = xmlNode.'@src';
        String fullPath = dir + file;

        int i = fullPath.lastIndexOf('/');
        Map fieldAttributes = [dir: fullPath.substring(0, i), file: fullPath.substring(i + 1)];
        fieldAttributes += xmlNode.attributes();

        log.debug("img Field Attributes ${fieldAttributes}");

        def tagBody = {
            g.img(fieldAttributes);
        }
        renderTag(fieldAttributes, tagBody);
    }

    def textEntryInteraction = {  attrs ->

        String tag = 'textEntryInteraction';
        Node xmlNode = getRequiredAttribute(attrs, 'xmlNode', tag);
        attrs += xmlNode.attributes();

        AssessmentItemInfo assessmentItemInfo = getRequiredAttribute(attrs, 'assessmentItemInfo', tag);
        Map responseValues = assessmentItemInfo.responseValues;

        String id = getRequiredAttribute(attrs, 'responseIdentifier', tag);
        String maxlength = getAttribute(attrs, 'expectedLength', tag);

        Map fieldAttributes = [name: id];

        if (maxlength) {
            fieldAttributes.maxlength = fieldAttributes.size = maxlength;
        }

        def value = responseValues[id];

        if (value) {
            fieldAttributes['value'] = value[0];
        }

        fieldAttributes += attrs;

        log.debug("AssessmentItemInfo ==> ${assessmentItemInfo}");
        log.debug("textEntryInteraction Field Attributes ${fieldAttributes}");

        def tagBody = {
            g.textField(fieldAttributes);
        }
        renderTag(attrs, tagBody);
    }

    def printedVariable = { attrs ->

        String tag = 'printedVariable';
        Map xmlAttributes = getRequiredAttribute(attrs, 'xmlAttributes', tag);

        Map templateValues = getOptionalAttribute(attrs, 'templateValues');
        Map outcomeValues = getOptionalAttribute(attrs, 'outcomeValues');

        List templateDeclarations = getOptionalAttribute(attrs, 'templateDeclarations');
        List outcomeDeclarations = getOptionalAttribute(attrs, 'outcomeDeclarations');

        String id = xmlAttributes.identifier;

        log.debug("${tag} attributes templateValues => ${templateValues} ; outcomeValues => ${outcomeValues} ; id => ${id}");
        String templateValue = templateValues?.get(id);
        String outcomeValue = outcomeValues?.get(id);
        String format = xmlAttributes.format;

        if (templateValue) {
            if (format) {
                out << " ${QtiUtils.formatVariable(QtiUtils.findVariableDeclarationByIdentifier(templateDeclarations, id), format, templateValue)} ";
            } else {
                out << " ${templateValue} ";
            }
        } else if (outcomeValue) {
            if (format) {
                out << " ${QtiUtils.formatVariable(QtiUtils.findVariableDeclarationByIdentifier(outcomeDeclarations, id), format, outcomeValue)} ";
            } else {
                out << " ${outcomeValue} ";
            }
        }
    }

    def endAttemptInteraction = { attrs ->
        String tag = 'endAttemptInteraction';
        Map xmlAttributes = getRequiredAttribute(attrs, 'xmlAttributes', tag);
        String id = xmlAttributes.responseIdentifier;
        String title = xmlAttributes.title;
        AssessmentItemInfo assessmentItemInfo = getRequiredAttribute(attrs, 'assessmentItemInfo', tag);
        assessmentItemInfo.addDisableOnCompletionId(id);

        Map actionParams = [id: params.id, (id): title];
        log.info("${tag} button action params => ${actionParams}");
        out << """ <input type='button' id='${id}' name='${id}' value='${title}' onclick="${remoteFunction(action: AssessmentItemInfo.controllerActionForProcessItem, params: actionParams, onSuccess: AssessmentItemInfo.onSuccessCallbackForProcessItem)}" />  """
    }

    def choiceInteraction = {  attrs ->

        String uitag = 'choiceInteraction';
        Node xmlNode = getRequiredAttribute(attrs, 'xmlNode', uitag);
        attrs += xmlNode.attributes();

        AssessmentItemInfo assessmentItemInfo = getRequiredAttribute(attrs, 'assessmentItemInfo', uitag);

        Map responseValues = assessmentItemInfo.responseValues
        Map outcome = assessmentItemInfo.outcomeValues;

        String id = getRequiredAttribute(attrs, 'responseIdentifier', uitag);
        String maxChoices = getRequiredAttribute(attrs, 'maxChoices', uitag)

        boolean shuffle = getOptionalAttribute(attrs, 'shuffle')?.toBoolean();

        Node prompt;
        List values = [];

        Map fixedChoices = [:];    //<position, choice >
        List shuffledChoices = [];
        List allChoices = [];
        int position = 0;
        xmlNode.children().each {child ->
            Tag tag = Tag.valueOf(child.name());
            switch (tag) {
                case Tag.prompt: prompt = child;
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

        fieldAttributes += attrs;

        def value = responseValues[id];

        log.debug("choiceInteraction Field Attributes ${fieldAttributes}");

        if (maxChoices.toInteger() == 1) {
            if (value) {
                fieldAttributes['value'] = value[0];
            }
            def tagBody = {
                if (prompt) {
                    out << g.render(template: '/renderer/renderItemSubTree', model: [node: prompt, assessmentItemInfo: assessmentItemInfo]);
                }
                g.radioGroup(fieldAttributes) {
                    out << "<p> ${it.radio}";
                    out << g.render(template: '/renderer/renderItemSubTree', model: [node: it.label, assessmentItemInfo: assessmentItemInfo]);
                    out << " </p> ";
                };

            }
            renderTag(attrs, tagBody);
        } else {
            if (prompt) {
                out << g.render(template: '/renderer/renderItemSubTree', model: [node: prompt, assessmentItemInfo: assessmentItemInfo]);
            }

            values.eachWithIndex {v, i ->
                boolean checked = (value && value.contains(v));
                out << "<p>";
                out << g.checkBox(name: id, value: v, checked: checked);
                out << g.render(template: '/renderer/renderItemSubTree', model: [node: allChoices[i], assessmentItemInfo: assessmentItemInfo]);
                out << " </p> ";
            }
        }

    }

    def inlineChoiceInteraction = {  attrs ->

        String tag = 'inlineChoiceInteraction';
        Node xmlNode = getRequiredAttribute(attrs, 'xmlNode', tag);
        attrs += xmlNode.attributes();

        AssessmentItemInfo assessmentItemInfo = getRequiredAttribute(attrs, 'assessmentItemInfo', tag);

        Map responseValues = assessmentItemInfo.responseValues;
        Map outcome = assessmentItemInfo.outcomeValues;

        String id = getRequiredAttribute(attrs, 'responseIdentifier', tag);

        boolean shuffle = getOptionalAttribute(attrs, 'shuffle')?.toBoolean();

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

        Map fieldAttributes = [name: id, keys: keys, from: from] + attrs;

        def value = responseValues[id];

        if (value) {
            fieldAttributes['value'] = value[0];
        }
        log.debug("inlineChoiceInteraction ${fieldAttributes}")

        def tagBody = {
            g.select(fieldAttributes);
        }
        renderTag(attrs, tagBody);

    }

    def hiddenElement = {  attrs ->

        String tag = 'hiddenElement';
        Node xmlNode = getRequiredAttribute(attrs, 'xmlNode', tag);
        AssessmentItemInfo assessmentItemInfo = getRequiredAttribute(attrs, 'assessmentItemInfo', tag);
        Tag xmlTag = getRequiredAttribute(attrs, 'xmlTag', tag);

        String title = xmlNode.'@title';
        String identifier = xmlNode.'@identifier';
        String valueLookupKey;
        if (Tag.isFeedBackTag(xmlTag)) {
            valueLookupKey = xmlNode.'@outcomeIdentifier';
        } else {
            valueLookupKey = xmlNode.'@templateIdentifier';
        }
        String visibilityMode = xmlNode.'@showHide';
        HiddenElement hiddenElement = assessmentItemInfo.addHiddenElement(new HiddenElement(identifier, valueLookupKey, xmlTag, visibilityMode));

        String sectionTag = (Tag.isInlineTag(xmlTag)) ? 'span' : 'div';
        Map sectionTagAttributes = [id: hiddenElement.elementId];
        boolean isModelFeedback = xmlTag == Tag.modalFeedback;
        if (isModelFeedback) {
            sectionTagAttributes['class'] = 'alert alert-success';
        }
        if (!assessmentItemInfo.isVisible(hiddenElement)) {
            sectionTagAttributes['style'] = 'display: none';
        }
        out << "<${sectionTag} ";
        sectionTagAttributes.each { k, v ->
            out << "${k}='${v}' ";
        }
        out << ">";
        if (isModelFeedback) {
            out << "<a class='close' data-dismiss='alert' href='#'>&times;</a>"
            if (title) {
                out << "<h4> ${title}! </h4>";
            }
        }
        out << g.render(template: '/renderer/renderItemSubTree', model: [node: xmlNode, assessmentItemInfo: assessmentItemInfo]);
        out << "</${sectionTag}> ";
    }

    def submit = { attrs ->
        String tag = "submit"
        AssessmentItemInfo assessmentItemInfo = getRequiredAttribute(attrs, 'assessmentItemInfo', tag);
        String id = attrs.name;
        String value = attrs.value;
        assessmentItemInfo.addDisableOnCompletionId(id);
        out << """ <button type="submit" class="btn btn-primary" id="${id}">
        									<i class="icon-ok icon-white"></i>
        									${value}
        								</button>  """
    }

    def mathML = {  attrs ->
        String tag = 'mathml';
        Node xmlNode = getRequiredAttribute(attrs, 'xmlNode', tag);
        Map templateValues = getOptionalAttribute(attrs, 'templateValues');

        if (templateValues) {
            xmlNode = QtiUtils.applyTemplateValuesInMathML(xmlNode, templateValues)
        }

        def sw = new StringWriter()
        new XmlNodePrinter(new PrintWriter(sw)).print(xmlNode);

        out << sw;
    }

    def assessmentSection = { attrs ->
        String tag = "assessmentSection";
        Node node = getRequiredAttribute(attrs, 'sectionTitles', tag);
        String sectionTitle = "";
        String parentTitle = null;

        node.children().each { Node title ->
            sectionTitle = SectionPartStatus.formatParentSection(parentTitle, title.text());
            parentTitle = sectionTitle
        }

        out << """ <div id="${Constants.testSectionTitleContent}" class="row-fluid">
                       <div class="breadcrumb">
                            <h4> ${sectionTitle} </h4>
                       </div>
                   </div> """
    }

    def less2Css = { attrs ->
         // com.rialms.util.Less2Css.run();
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

    protected getOptionalAttribute(attrs, String name) {
        return getAttribute(attrs, name, null, false);
    }
}