package com.rialms.taglib

import com.rialms.consts.Constants as Consts

import com.rialms.angular.JsObjectUtil
import com.rialms.assessment.item.AssessmentItemInfo
import com.rialms.assessment.render.HiddenElement
import com.rialms.assessment.test.SectionPartStatus
import com.rialms.consts.EndAttemptButton
import com.rialms.consts.Tag
import com.rialms.util.QtiUtils
import grails.util.Environment
import com.rialms.util.CollectionUtils
import java.text.DecimalFormat
import java.text.NumberFormat
import org.qtitools.qti.node.item.Stylesheet
import org.qtitools.qti.node.item.interaction.Interaction
import com.rialms.assessment.item.InteractionHelp
import grails.converters.JSON


class QtiTagLib {
    static namespace = "qti";
    private static final NumberFormat scoreFormat = new DecimalFormat("##.#");

    //TODO: P3 fix excessive new lines
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


    def stylesheet = {  attrs ->

        String tag = "stylesheet";
        AssessmentItemInfo assessmentItemInfo = getRequiredAttribute(attrs, 'assessmentItemInfo', tag);
        String dir = assessmentItemInfo.dataPath;

        List<Stylesheet> stylesheets = assessmentItemInfo.assessmentItem.stylesheets;

        stylesheets.each { stylesheet ->
            String file = stylesheet.getHref();
            String fullPath = dir + file;
            int i = fullPath.lastIndexOf('/');
            Map fieldAttributes = [dir: fullPath.substring(0, i), file: fullPath.substring(i + 1)];

            String media = stylesheet.getMedia();
            String title = stylesheet.getTitle();
            String type = stylesheet.getTitle();

            if (media) {
                fieldAttributes[media] = media;
            }
            if (type) {
                fieldAttributes[type] = type;
            }
            if (media) {
                fieldAttributes[title] = title;
            }

            log.debug("DEBUG Stylesheet Field Attributes ${fieldAttributes} ${g.external(fieldAttributes).toString()}");
            def tagBody = {
                g.external(fieldAttributes);
            }
            renderTag(attrs, tagBody);
        }
    }

    def textEntryInteraction = {  attrs ->

        String tag = 'textEntryInteraction';
        Node xmlNode = getRequiredAttribute(attrs, 'xmlNode', tag);
        attrs += xmlNode.attributes();

        AssessmentItemInfo assessmentItemInfo = getRequiredAttribute(attrs, 'assessmentItemInfo', tag);
        Map responseValues = assessmentItemInfo.responseValues;

        String id = getRequiredAttribute(attrs, 'responseIdentifier', tag);
        String maxlength = getOptionalAttribute(attrs, 'expectedLength');
        String placeHolder = getOptionalAttribute(attrs, 'placeholderText');

        Map fieldAttributes = [name: id];

        if (maxlength) {
            fieldAttributes.maxlength = fieldAttributes.size = maxlength;
        }
        if (placeHolder) {
            fieldAttributes.placeholder = placeHolder;
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

        out << """<div ng-init="${new JsObjectUtil.PropertyConstructor(Consts.endAttemptButtons).getProperty(id)}='${title}'"></div>"""

        assessmentItemInfo.addEndAttemptButton(id, title);
    }

    def itemResult = { attrs ->
        String tag = 'itemResult';
        AssessmentItemInfo assessmentItemInfo = getRequiredAttribute(attrs, 'assessmentItemInfo', tag);
        double score = assessmentItemInfo.getScore();
        String scoreString = scoreFormat.format(score);

        if (assessmentItemInfo.isCorrect() && score > 0) {
            out << """<span class="alert item-result result-correct">"""
            out << g.message(code: 'itemResult.correct.message', args: [scoreString]);
            out << "</span>"
        }
        else if (!assessmentItemInfo.adaptive) {
            if (score > 0) {
                out << """<span class="alert item-result result-partiallyCorrect">"""
                out << g.message(code: 'itemResult.partiallyCorrect.message', args: [scoreString]);
                out << "</span>"
            } else {
                out << """<span class="alert item-result result-incorrect">"""
                out << g.message(code: 'itemResult.incorrect.message');
                out << "</span>"
            }
        }
    }

    def headerButton = {attrs ->
        String tag = "headerButton";
        EndAttemptButton type = getRequiredAttribute(attrs, 'type', tag);
        String buttonIdentifier, buttonObject, title, iconClass;
        buttonIdentifier = grailsApplication.config.rialms[type.configIdentifier()];
        iconClass = type.iconClass;
        JsObjectUtil.PropertyConstructor props = new JsObjectUtil.PropertyConstructor(Consts.endAttemptButtons)
        buttonObject = props.getProperty(buttonIdentifier);
        title = JsObjectUtil.getTemplateVar(buttonObject);
        Map fieldAttributes = [action: AssessmentItemInfo.controllerActionForProcessItem,
                onSuccess: AssessmentItemInfo.onSuccessCallbackForProcessItem,
                title: title];

        fieldAttributes.params = ['id': params.id, (buttonIdentifier): title];
        fieldAttributes.'ng-hide' = "!${buttonObject}";

        def tagBody = {
            g.remoteLink(fieldAttributes) {
                "<i class='${iconClass}'></i>&nbsp;&nbsp;"
            }
        }
        renderTag(attrs, tagBody);
    }

    def endAttemptButton = { attrs ->
        String tag = "endAttemptButton";
        String buttonIdentifier = getRequiredAttribute(attrs, 'buttonIdentifier', tag);
        String title = getRequiredAttribute(attrs, 'buttonTitle', tag);
        EndAttemptButton endAttemptButton = getEndAttemptButton(buttonIdentifier);
        AssessmentItemInfo assessmentItemInfo = getRequiredAttribute(attrs, 'assessmentItemInfo', tag);

        String iconClass;
        iconClass = endAttemptButton.iconClass;
        //TODO P2: Do validation for multi hint
        String multiHintClickCount = assessmentItemInfo.getMultiHintClickCount().toString();
        String multiHintStepCount = assessmentItemInfo.getMultiHintStepCount().toString();
        String multiHintRemainingCount = assessmentItemInfo.getMultiHintRemainingCount().toString();
        String remainingHintMessage = g.message(code: 'hint.remaining.message');
        log.debug("DEBUG multiHintClickCount = ${multiHintClickCount} ; multiHintStepCount = ${multiHintStepCount} ; multiHintRemainingCount = ${assessmentItemInfo.multiHintRemainingCount}")

        Map fieldAttributes = [
                onSuccess: AssessmentItemInfo.onSuccessCallbackForProcessItem,
                'ng-class': 'getMultiHintStyle()', 'ng-click': 'multiHintClicked()', 'ng-init': "multiHintClickCount=${multiHintClickCount};multiHintStepCount=${multiHintStepCount};multiHintRemainingCount=${multiHintRemainingCount}"];

        //if multiHintStep is present, make endAttemptButton actionable only if multiHint remaining is present as well
        if (assessmentItemInfo.multiHintStepCount > 0 && assessmentItemInfo.multiHintRemainingCount <= 0) {
            fieldAttributes['href'] = '';
        } else {
            fieldAttributes['action'] = AssessmentItemInfo.controllerActionForProcessItem;
        }

        fieldAttributes.params = ['id': params.id, (buttonIdentifier): title];

        def tagBody = {
            g.remoteLink(fieldAttributes) {
                "<i class='${iconClass}'></i>&nbsp;&nbsp;${title}&nbsp;<span ng-show='multiHintStepCount > 0 && multiHintRemainingCount >= 0'>({{multiHintRemainingCount}} ${remainingHintMessage})</span>"
            }
        }
        renderTag(attrs, tagBody);
    }

    def endAttemptButtons = { attrs ->
        String tag = "endAttemptButtons"

        AssessmentItemInfo assessmentItemInfo = getRequiredAttribute(attrs, 'assessmentItemInfo', tag);

        assessmentItemInfo.endAttemptButtons.each { key, value ->
            out << endAttemptButton(buttonIdentifier: key, buttonTitle: value, assessmentItemInfo: assessmentItemInfo);
        }
    }

    def choiceInteraction = {  attrs ->

        String uitag = 'choiceInteraction';
        Node xmlNode = getRequiredAttribute(attrs, 'xmlNode', uitag);
        attrs += xmlNode.attributes();

        AssessmentItemInfo assessmentItemInfo = getRequiredAttribute(attrs, 'assessmentItemInfo', uitag);

        Map responseValues = assessmentItemInfo.responseValues

        String id = getRequiredAttribute(attrs, 'responseIdentifier', uitag);
        String maxChoices = getRequiredAttribute(attrs, 'maxChoices', uitag)

        boolean shuffle = getOptionalAttribute(attrs, 'shuffle')?.toBoolean();

        Node prompt = null;
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
                    if (!assessmentItemInfo.checkForHiddenElement(child, Tag.simpleChoice)) {
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
                    }

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

        log.debug("response = ${value}")
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

    def orderInteraction = {  attrs ->

        String uitag = 'orderInteraction';
        Node xmlNode = getRequiredAttribute(attrs, 'xmlNode', uitag);
        attrs += xmlNode.attributes();

        AssessmentItemInfo assessmentItemInfo = getRequiredAttribute(attrs, 'assessmentItemInfo', uitag);


        String id = getRequiredAttribute(attrs, 'responseIdentifier', uitag);
        List<String> responseValues = assessmentItemInfo.responseValues[id].collect {it.toString()};
        log.debug("responseValues == ${responseValues}")

        boolean shuffle = getOptionalAttribute(attrs, 'shuffle')?.toBoolean();

        Node prompt = null;

        Map fixedChoices = [:];    //<position, choice >
        Map responsePositions = [:] //<position , choice >  //Used to order items if it was already submitted
        List shuffledChoices = [];
        List allChoices = [];
        int position = 0;
        xmlNode.children().each {child ->
            Tag tag = Tag.valueOf(child.name());
            switch (tag) {
                case Tag.prompt: prompt = child;
                    break;

                case Tag.simpleChoice:
                    if (!assessmentItemInfo.checkForHiddenElement(child, Tag.simpleChoice)) {
                        if (!responseValues.isEmpty()) {
                            responsePositions[responseValues.indexOf(child.attribute('identifier'))] = child;
                        }
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
                    }
                    break;
            }
        }
        allChoices = (shuffle) ? CollectionUtils.shuffleWithFixedPositions(shuffledChoices, fixedChoices) : CollectionUtils.orderValuesByPosition(fixedChoices);
        log.debug("responsePositions ${responsePositions}");
        if (!responsePositions.isEmpty()) {
            allChoices = CollectionUtils.orderValuesByPosition(responsePositions);
        }


        log.debug("All Choices ${allChoices}");

        if (prompt) {
            out << g.render(template: '/renderer/renderItemSubTree', model: [node: prompt, assessmentItemInfo: assessmentItemInfo]);
        }
        out << """<ol class="order-interaction">""";
        allChoices.each { choice ->
            out << "<li><span title='${g.message(code: 'order.tooltip')}'>"
            out << g.render(template: '/renderer/renderItemSubTree', model: [node: choice, assessmentItemInfo: assessmentItemInfo]);
            out << """<input id="${id}" name="${id}" type="hidden" value="${choice.attribute('identifier')}" />"""
            out << "</span></li>"
        }
        out << "</ol>"
    }

    def inlineChoiceInteraction = {  attrs ->

        String tag = 'inlineChoiceInteraction';
        Node xmlNode = getRequiredAttribute(attrs, 'xmlNode', tag);
        attrs += xmlNode.attributes();

        AssessmentItemInfo assessmentItemInfo = getRequiredAttribute(attrs, 'assessmentItemInfo', tag);

        Map responseValues = assessmentItemInfo.responseValues;

        String id = getRequiredAttribute(attrs, 'responseIdentifier', tag);

        boolean shuffle = getOptionalAttribute(attrs, 'shuffle')?.toBoolean();

        Map fixedChoices = [:];    //<position, choice >
        List shuffledChoices = [];
        List allChoices = [];
        List from = [];
        List keys = [];
        int position = 0;
        xmlNode.children().each {Node child ->
            if (!assessmentItemInfo.checkForHiddenElement(child, Tag.inlineChoice)) {
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

    def gapMatchInteraction = {attrs ->
        String uitag = 'gapMatchInteraction';

        Node xmlNode = getRequiredAttribute(attrs, 'xmlNode', uitag);
        attrs += xmlNode.attributes();

        AssessmentItemInfo assessmentItemInfo = getRequiredAttribute(attrs, 'assessmentItemInfo', uitag);
        boolean shuffle = getRequiredAttribute(attrs, 'shuffle', uitag)?.toBoolean();

        Node prompt;

        Map fixedChoices = [:];    //<position, choice >
        List shuffledChoices = [];
        List allChoices = [];
        List contents = [];
        int position = 0;
        xmlNode.children().each {Node child ->
            Tag tag = Tag.valueOf(child.name());
            switch (tag) {
                case Tag.prompt: prompt = child;
                    break;

                case Tag.gapText:
                    if (!assessmentItemInfo.checkForHiddenElement(child, Tag.inlineChoice)) {
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
                    }
                    break;
                default: contents << child;
                    break;

            }
        }

        if (shuffle) {
            allChoices = CollectionUtils.shuffleWithFixedPositions(shuffledChoices, fixedChoices);
        } else {
            allChoices = CollectionUtils.orderValuesByPosition(fixedChoices);
        }

        contents.each { content ->
            out << g.render(template: '/renderer/renderItemSubTree', model: [node: xmlNode, assessmentItemInfo: assessmentItemInfo]);
        }

        out << "<hr/>";
        out << "<p>";
        out << "<span class='gap-choices'>"
        out << g.message(code: 'gapText.title');
        out << "</span>"
        allChoices.each { choice ->
            out << gapText('xmlNode': choice, 'assessmentItemInfo': assessmentItemInfo);
        }
        out << "</p>";
    }

    def gapText = {attrs ->
        String tag = 'gapText';
        Node xmlNode = getRequiredAttribute(attrs, 'xmlNode', tag);

        attrs += xmlNode.attributes();
        AssessmentItemInfo assessmentItemInfo = getRequiredAttribute(attrs, 'assessmentItemInfo', tag);

        String responseIdentifier = QtiUtils.findParentByTag(xmlNode, Tag.gapMatchInteraction).attribute(Consts.responseIdentifier);
        List responseValues = assessmentItemInfo.responseValues[responseIdentifier];
        int responseCount = 0;

        String identifier = getRequiredAttribute(attrs, 'identifier', tag)

        if (responseValues && !responseValues.isEmpty()) {
            responseCount = QtiUtils.convertMultipleResponseValuesToMap(responseValues).values().count {responseValue -> responseValue == identifier}
        }

        int matchMax = getRequiredAttribute(attrs, 'matchMax', tag) as int;
        log.debug("responseValues = ${responseValues} ; responseCount = ${responseCount}; matchMax = ${matchMax}")
        int remainingCount = (matchMax == 0) ? 0 : (matchMax - responseCount)
        if (matchMax == 0 || remainingCount > 0) {
            Tag xmlTag = Tag.gapText;
            Map tagAttributes = [class: 'draggable-gap-text'];
            String dataAttributes = CollectionUtils.convertMapToDataAttributes([identifier: identifier, matchMax: (remainingCount), 'original-title': g.message(code: 'gapText.tooltip')])
            out << """<span ${CollectionUtils.convertMapToAttributes(tagAttributes)} ${dataAttributes} class='draggable'> """
            String gapTextValue = g.render(template: '/renderer/renderItemSubTree', model: [node: xmlNode, assessmentItemInfo: assessmentItemInfo]).toString();
            out << gapTextValue;
            assessmentItemInfo.addParam("${Tag.gapMatchInteraction.name()}.${identifier}.${Consts.value}", gapTextValue)
            out << "</span>"
        }
    }

    def gap = {attrs ->
        String tag = 'gap';
        Node xmlNode = getRequiredAttribute(attrs, 'xmlNode', tag);
        attrs += xmlNode.attributes();
        AssessmentItemInfo assessmentItemInfo = getRequiredAttribute(attrs, 'assessmentItemInfo', tag);
        String identifier = getRequiredAttribute(attrs, 'identifier', tag);
        String responseIdentifier = QtiUtils.findParentByTag(xmlNode, Tag.gapMatchInteraction).attribute(Consts.responseIdentifier);
        List responseValues = assessmentItemInfo.responseValues[responseIdentifier];
        String responseValue = ''
        if (responseValues && !responseValues.isEmpty()) {
            responseValue = QtiUtils.convertMultipleResponseValuesToMap(responseValues)[identifier];
        }
        String displayValue = '&nbsp'
        String inputValue = '';
        if (responseValue) {
            displayValue = assessmentItemInfo.getParam("${Tag.gapMatchInteraction.name()}.${responseValue}.${Consts.value}");
            inputValue = "${responseValue} ${identifier}";
        }
        log.debug("DEBUG ${tag} => responseValue = ${responseValue}")
        String dataAttributes = CollectionUtils.convertMapToDataAttributes([identifier: identifier, 'original-title': g.message(code: 'gap.tooltip')]);
        out << """<span ${dataAttributes} class='droppable-gap'>"""
        out << """<input type='hidden' name="${responseIdentifier}" value="${inputValue}" /> """
        out << "<span>${displayValue}</span></span>";
    }

    def matchInteraction = {attrs ->
        String uitag = 'matchInteraction';

        Node xmlNode = getRequiredAttribute(attrs, 'xmlNode', uitag);
        attrs += xmlNode.attributes();

        AssessmentItemInfo assessmentItemInfo = getRequiredAttribute(attrs, 'assessmentItemInfo', uitag);

        String responseIdentifier = getRequiredAttribute(attrs, Consts.responseIdentifier, uitag);
        List responseValues = assessmentItemInfo.responseValues[responseIdentifier];
        //TODO p3: maxAssociations and minAssociations attributes for matchInteraction are not used.

        boolean shuffle = getRequiredAttribute(attrs, 'shuffle', uitag)?.toBoolean();

        Node prompt;
        List<Map> fixedChoices = [];    //list of <position, choice>, one of lhs and one for rhs
        List<List> shuffledChoices = [];
        List<List> allChoices = [];
        int position = 0;
        int matchSetIndex = 0;
        xmlNode.children().each {child ->
            Tag tag = Tag.valueOf(child.name());
            switch (tag) {
                case Tag.prompt: prompt = child;
                    break;

                case Tag.simpleMatchSet:
                    position = 0;
                    fixedChoices[matchSetIndex] = [:];
                    shuffledChoices[matchSetIndex] = [];
                    allChoices[matchSetIndex] = [];
                    child.children().each { associableChoice ->
                        if (!assessmentItemInfo.checkForHiddenElement(associableChoice, Tag.simpleAssociableChoice)) {
                            if (associableChoice.attribute("fixed")?.toBoolean()) {
                                fixedChoices[matchSetIndex][position] = associableChoice;
                            } else {
                                if (shuffle) {
                                    shuffledChoices[matchSetIndex] << associableChoice;
                                } else {
                                    fixedChoices[matchSetIndex][position] = associableChoice;
                                }
                            }
                            position++;
                        }

                    }
                    matchSetIndex++;
                    break;
            }
        }

        if (shuffle) {
            fixedChoices.eachWithIndex {fixedChoice, index ->
                allChoices[index] = CollectionUtils.shuffleWithFixedPositions(shuffledChoices[index], fixedChoices[index]);
            }

        } else {
            fixedChoices.eachWithIndex { fixedChoice, index ->
                allChoices[index] = CollectionUtils.orderValuesByPosition(fixedChoice);
            }

        }

        final int LHS = 0;
        final int RHS = 1;
        log.debug("All LHS choices ${allChoices[0]}")
        log.debug("All RHS choices ${allChoices[1]}")

        out << """<div class='match-interaction'>""";

        log.debug("responseValues ${responseValues}")
        responseValues.each { responseValue ->
            out << """<input type='hidden' name="${responseIdentifier}" value="${responseValue}" /> """
        }

        if (prompt) {
            out << g.render(template: '/renderer/renderItemSubTree', model: [node: prompt, assessmentItemInfo: assessmentItemInfo]);
        }
        out << "<br/><br/>"
        int lhsSize = allChoices[LHS].size()
        int rhsSize = allChoices[RHS].size();
        int rowCount = Math.max(lhsSize, rhsSize);
        Node choice;
        String dataAttributes;

        for (int i = 0; i < rowCount; i++) {
            out << """<div class="row-fluid">"""
            out << """<div class="span6 ">"""
            if (i < lhsSize) {
                choice = allChoices[LHS][i];
                dataAttributes = CollectionUtils.convertMapToDataAttributes(
                        [
                                identifier: choice.attribute('identifier'),
                                matchMax: choice.attribute('matchMax'),
                                (Consts.responseIdentifier): responseIdentifier,
                                (Consts.role): Consts.source,
                                'original-title': g.message(code: 'associableChoice.tooltip')
                        ]
                );
                out << """<span class="associable-choice" ${dataAttributes} >""";
                out << g.render(template: '/renderer/renderItemSubTree', model: [node: choice, assessmentItemInfo: assessmentItemInfo]);
                out << "</span>";
            }
            out << "</div>"
            out << """<div class="span6 ">"""
            if (i < rhsSize) {
                choice = allChoices[RHS][i];
                dataAttributes = CollectionUtils.convertMapToDataAttributes(
                        [
                                identifier: choice.attribute('identifier'),
                                matchMax: choice.attribute('matchMax'),
                                (Consts.responseIdentifier): responseIdentifier,
                                (Consts.role): Consts.target,
                                'original-title': g.message(code: 'associableChoice.tooltip')
                        ]
                );
                out << """<span class="associable-choice" ${dataAttributes} >""";
                out << g.render(template: '/renderer/renderItemSubTree', model: [node: choice, assessmentItemInfo: assessmentItemInfo]);
                out << "</span>"
            }
            out << "</span></div>"
        }
        out << "</div>"
    }

    def associateInteraction = {attrs ->
        String uitag = 'associateInteraction';

        Node xmlNode = getRequiredAttribute(attrs, 'xmlNode', uitag);
        attrs += xmlNode.attributes();

        AssessmentItemInfo assessmentItemInfo = getRequiredAttribute(attrs, 'assessmentItemInfo', uitag);

        String responseIdentifier = getRequiredAttribute(attrs, Consts.responseIdentifier, uitag);
        List responseValues = assessmentItemInfo.responseValues[responseIdentifier];

        log.debug("responseValues ${responseValues}")

        //TODO p3: maxAssociations and minAssociations attributes for matchInteraction are not used.

        boolean shuffle = getRequiredAttribute(attrs, 'shuffle', uitag)?.toBoolean();

        Node prompt;
        Map fixedChoices = [:];    //<position, choice >
        List shuffledChoices = [];
        List allChoices = [];
        int position = 0;
        xmlNode.children().each {child ->
            Tag tag = Tag.valueOf(child.name());
            switch (tag) {
                case Tag.prompt: prompt = child;
                    break;
                case Tag.simpleAssociableChoice:
                    if (!assessmentItemInfo.checkForHiddenElement(child, Tag.simpleAssociableChoice)) {
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
                    }
                    break;
            }
        }
        if (shuffle) {
            allChoices = CollectionUtils.shuffleWithFixedPositions(shuffledChoices, fixedChoices);
        } else {
            allChoices = CollectionUtils.orderValuesByPosition(fixedChoices);
        }
        out << """<div class='associate-interaction' data-initialized='false' >""";

        responseValues.each { responseValue ->
            out << """<input type='hidden' name="${responseIdentifier}" value="${responseValue}" /> """
        }

        if (prompt) {
            out << g.render(template: '/renderer/renderItemSubTree', model: [node: prompt, assessmentItemInfo: assessmentItemInfo]);
        }
        out << "<br/><br/>"

        String dataAttributes;

        allChoices.each { choice ->
            out << """<div class="row-fluid">"""
            out << """<div class="span12">"""
            dataAttributes = CollectionUtils.convertMapToDataAttributes(
                    [
                            identifier: choice.attribute('identifier'),
                            matchMax: choice.attribute('matchMax'),
                            (Consts.responseIdentifier): responseIdentifier,
                            (Consts.role): Consts.sourceAndTarget,
                            'original-title': g.message(code: 'associableChoice.tooltip')
                    ]
            );
            out << """<span class="associable-choice" ${dataAttributes} >""";
            out << g.render(template: '/renderer/renderItemSubTree', model: [node: choice, assessmentItemInfo: assessmentItemInfo]);
            out << """<span class='associable-choice-endpoint'>&nbsp;</span></span></div></div>""";
        }
        out << "</div>"
    }

    def hottextInteraction = {  attrs ->

        String tag = 'hottextInteraction';
        Node xmlNode = getRequiredAttribute(attrs, 'xmlNode', tag);
        AssessmentItemInfo assessmentItemInfo = getRequiredAttribute(attrs, 'assessmentItemInfo', tag);
        out << """<div>""";
        out << g.render(template: '/renderer/renderItemSubTree', model: [node: xmlNode, assessmentItemInfo: assessmentItemInfo]);
        out << """</div>""";
    }

    def hottext = {  attrs ->

        String tag = 'hottext';
        Node xmlNode = getRequiredAttribute(attrs, 'xmlNode', tag);
        attrs += xmlNode.attributes();
        AssessmentItemInfo assessmentItemInfo = getRequiredAttribute(attrs, 'assessmentItemInfo', tag);

        if (!assessmentItemInfo.checkForHiddenElement(xmlNode, Tag.hottext)) {
            String identifier = getRequiredAttribute(attrs, Consts.identifier, tag); ;
            Node hottextInteraction = QtiUtils.findParentByTag(xmlNode, Tag.hottextInteraction)
            String responseIdentifier = hottextInteraction.attribute(Consts.responseIdentifier);
            List responseValues = assessmentItemInfo.responseValues[responseIdentifier];
            log.debug("responseValues ${responseValues}");

            String maxChoices = hottextInteraction.attribute(Consts.maxChoices);
            String type = maxChoices.toInteger() == 1 ? 'radio' : 'checkbox';

            Map fieldAttributes = [type: type, name: responseIdentifier, value: identifier];
            boolean checked = responseValues && responseValues.contains(identifier);

            if (checked) {
                out << """<input ${CollectionUtils.convertMapToAttributes(fieldAttributes)} checked ><b>"""
            } else {
                out << """<input ${CollectionUtils.convertMapToAttributes(fieldAttributes)}><b>"""
            }

            out << g.render(template: '/renderer/renderItemSubTree', model: [node: xmlNode, assessmentItemInfo: assessmentItemInfo]);
            out << "</b></input>"
        } else {
            out << g.render(template: '/renderer/renderItemSubTree', model: [node: xmlNode, assessmentItemInfo: assessmentItemInfo]);
        }
    }

    def hiddenElement = {  attrs ->

        String tag = 'hiddenElement';
        Node xmlNode = getRequiredAttribute(attrs, 'xmlNode', tag);
        AssessmentItemInfo assessmentItemInfo = getRequiredAttribute(attrs, 'assessmentItemInfo', tag);
        Tag xmlTag = getRequiredAttribute(attrs, 'xmlTag', tag);

        String title = xmlNode.'@title';

        HiddenElement hiddenElement = assessmentItemInfo.addHiddenElement(xmlNode, xmlTag);
        log.debug("DEBUG Added hiddenElement ${hiddenElement}")
        String ngShowCondition = "${Consts.hiddenElementsData}.${hiddenElement.elementId}_${Consts.visible}";
        String sectionTag = (Tag.isInlineTag(xmlTag)) ? 'span' : 'div';
        Map sectionTagAttributes = ['ng-show': "${ngShowCondition}"];
        boolean isModelFeedback = xmlTag == Tag.modalFeedback;
        if (isModelFeedback) {
            sectionTagAttributes['class'] = 'well model-feedback';
        }
        out << "<${sectionTag} ${CollectionUtils.convertMapToAttributes(sectionTagAttributes)} >";

        if (isModelFeedback) {
            if (title) {
                out << "<h4> ${title}! </h4>";
            }
        }
        out << g.render(template: '/renderer/renderItemSubTree', model: [node: xmlNode, assessmentItemInfo: assessmentItemInfo]);
        out << "</${sectionTag}>";

    }

    def div = {  attrs ->

        String tag = 'div';
        Node xmlNode = getRequiredAttribute(attrs, 'xmlNode', tag);
        attrs += xmlNode.attributes();
        AssessmentItemInfo assessmentItemInfo = getRequiredAttribute(attrs, 'assessmentItemInfo', tag);
        String divAttributes = CollectionUtils.convertMapToAttributes(xmlNode.attributes())
        String divId = getOptionalAttribute(attrs, 'id');
        int hintNumber = 0;
        int multiHintClickCount = assessmentItemInfo.getMultiHintClickCount();
        if (divId && divId.startsWith(Consts.MULTI_HINT_PREFIX)) {
            hintNumber = (divId - Consts.MULTI_HINT_PREFIX).toInteger();
            assessmentItemInfo.incrementMultiHintStepCount();
            out << """<div ng-init="multiHintClickCount=${multiHintClickCount}" ng-show="multiHintClickCount>=${hintNumber}"  ${divAttributes}>"""
        }
        else {
            out << "<div ${divAttributes}>";
        }
        out << g.render(template: '/renderer/renderItemSubTree', model: [node: xmlNode, assessmentItemInfo: assessmentItemInfo]);
        out << "</div>";
    }

    def submit = { attrs ->
        String tag = "submit"
        AssessmentItemInfo assessmentItemInfo = getRequiredAttribute(attrs, 'assessmentItemInfo', tag);
        String id = attrs.name;
        String value = attrs.value;
        assessmentItemInfo.addDisableOnCompletionId(id);
        out << """<button type="submit" class="btn btn-success" id="${id}">
        									<i class="icon-ok icon-white"></i>
        									${value}
        								</button>"""
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

        out << """ <div id="${Consts.testSectionTitleContent}" class="row-fluid">
                       <div class="breadcrumb">
                            <h4> ${sectionTitle} </h4>
                       </div>
                   </div> """
    }

    def interactionHelp = {attrs ->
        String tag = 'interactionHelp';
        AssessmentItemInfo assessmentItemInfo = getRequiredAttribute(attrs, 'assessmentItemInfo', tag);

        out << "<div>";
        assessmentItemInfo.interactionHelps.each {interactionHelp ->
            out << """<div id='${interactionHelp.id}' style='display:none' > """
            out << g.img(dir: InteractionHelp.IMAGE_DIR, file: interactionHelp.imageFile);
            out << "</div>"
        }
        out << "</div>";
    }

    def helpButtons = {attrs ->
        String tag = 'helpButtons';
        AssessmentItemInfo assessmentItemInfo = getRequiredAttribute(attrs, 'assessmentItemInfo', tag);
        List<InteractionHelp> interactionHelps = assessmentItemInfo.interactionHelps;
        int size = interactionHelps.size();

        if (size > 0) {
            out << "<span class='pull-right btn-group'>";
            if (size > 1) {

                //out << """<a class="btn btn-info" href='#' onclick='window.showInteractionHelp(${params as JSON})' ><i class="icon-question-sign"></i>&nbsp;&nbsp;${params[Consts.title]}</a>"""
                out << """<a class="btn btn-info dropdown-toggle" data-toggle="dropdown"><i class="icon-question-sign"></i>&nbsp;&nbsp;"""
                out << g.message(code: 'help.title');
                out << """&nbsp;&nbsp;<span class="caret"></span>""";
                out << "</a>"
                out << """ <ul class="dropdown-menu"> """;
                interactionHelps.each{ interactionHelp->
                    out << "<li>"
                    Map params = getShowInteractionHelpParams(interactionHelp);
                    out << """<a href='#' onclick='window.showInteractionHelp(${params as JSON})' >${params[Consts.title]}</a>"""
                    out << "</li>"
                }
                out << "</ul>"


            }else{
                Map params = getShowInteractionHelpParams(interactionHelps[0]);
                out << """<a class="btn btn-info" href='#' onclick='window.showInteractionHelp(${params as JSON})' ><i class="icon-question-sign"></i>&nbsp;&nbsp;${params[Consts.title]}</a>"""
            }
            out << "</span>";
        }
    }

    def less2Css = { attrs ->
        if (Environment.currentEnvironment == Environment.DEVELOPMENT) {
             com.rialms.util.Less2Css.run();
        }
    }

    private void renderTag(Map fieldAttributes, Closure tagBody) {
        out << """  ${tagBody()} """;
    }

    protected getAttribute(Map attrs, String name, String tagName, boolean isRequired = false) {
        if (isRequired && !attrs.containsKey(name)) {
            throwTagError("Tag [$tagName] is missing required attribute [$name]")
        }
        attrs.remove name
    }

    protected getRequiredAttribute(Map attrs, String name, String tagName) {
        return getAttribute(attrs, name, tagName, true);
    }

    protected getOptionalAttribute(Map attrs, String name) {
        return getAttribute(attrs, name, null, false);
    }

    private Map<String, String> getShowInteractionHelpParams(InteractionHelp interactionHelp) {
        Map params = [(Consts.title): g.message(code: interactionHelp.titleMessageCode), (Consts.height): interactionHelp.height, (Consts.width): interactionHelp.width, (Consts.elementId): "#${interactionHelp.id}"];
        return params;
    }

    private EndAttemptButton getEndAttemptButton(String buttonIdentifier) {
        (buttonIdentifier == hintIdentifier) ? EndAttemptButton.hint : (buttonIdentifier == solutionIdentifier ? EndAttemptButton.solution : EndAttemptButton.other)
    }

    private String getHintIdentifier() {
        return grailsApplication.config.rialms.hintIdentifier;
    }

    private String getSolutionIdentifier() {
        return grailsApplication.config.rialms.solutionIdentifier;
    }
}