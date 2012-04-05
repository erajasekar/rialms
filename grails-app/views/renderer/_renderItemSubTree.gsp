<%--
  Created by IntelliJ IDEA.
  User: kavi
  Date: 3/13/12
  Time: 10:47 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page import="com.rialms.assessment.render.HiddenElement; com.rialms.assessment.item.AssessmentItemInfo; com.rialms.consts.Tag" contentType="text/html;charset=UTF-8" %>
<g:each var="n" in="${node.children()}">

    <g:if test="${n instanceof String}">
        ${n}
    </g:if>
    <g:else>

        <% def tag = Tag.valueOf(n.name()); %>

        <g:if test="${Tag.isMixedTag(tag)}">
            <${n.name().getLocalPart()}>
            <g:render template="/renderer/renderItemSubTree" model="[node: n]"/>
            </${n.name().getLocalPart()}>
        </g:if>

        <g:if test="${Tag.isFlowThroughTag(tag)}">
            <p>
                <g:render template="/renderer/renderItemSubTree" model="[node: n]"/>
            </p>
        </g:if>
        <g:if test="${tag == Tag.rubricBlock}"><!-- TODO ADD VIEW SUPPORT FOR RUBRIC -->
            <g:render template="/renderer/renderItemSubTree" model="[node: n]"/>
        </g:if>
        <g:elseif test="${tag == Tag.img}">
            <qti:img assessmentItemInfo="${assessmentItemInfo}" xmlNode="${n}" file="${n.'@src'}" alt="${n.'@alt'}"/>
        </g:elseif>

        <g:elseif test="${tag == Tag.textEntryInteraction}">
            <qti:textEntryInteraction xmlNode="${n}" assessmentItemInfo="${assessmentItemInfo}"/>
        </g:elseif>

        <g:elseif test="${tag == Tag.choiceInteraction}">
            <qti:choiceInteraction xmlNode="${n}" assessmentItemInfo="${assessmentItemInfo}"
                                   dataPath="${dataPath}"/>
        </g:elseif>

        <g:elseif test="${tag == Tag.inlineChoiceInteraction}">
            <qti:inlineChoiceInteraction xmlNode="${n}" assessmentItemInfo="${assessmentItemInfo}"/>
        </g:elseif>

        <g:elseif test="${tag == Tag.printedVariable}">
            <g:if test="${assessmentItemInfo && !assessmentItemInfo.is(AssessmentItemInfo.BLANK_ITEM)}">
                <qti:printedVariable xmlAttributes="${n.attributes()}"
                                     templateValues="${assessmentItemInfo.templateValues}"
                                     templateDeclarations="${assessmentItemInfo.templateDeclarations}"
                                     outcomeValues="${assessmentItemInfo.outcomeValues}"
                                     outcomeDeclarations="${assessmentItemInfo.outcomeDeclarations}"/>
            </g:if>
            <g:elseif test="${assessmentParams}">
                <qti:printedVariable xmlAttributes="${n.attributes()}"
                                     outcomeValues="${assessmentParams.outcomeValues}"
                                     outcomeDeclarations="${assessmentParams.outcomeDeclarations}"/>
            </g:elseif>
            <g:else>
                Error Both  assessmentItemInfo and assessmentParams is null
            </g:else>

        </g:elseif>

        <g:elseif test="${tag == Tag.endAttemptInteraction}">
            <qti:endAttemptInteraction xmlAttributes="${n.attributes()}" assessmentItemInfo="${assessmentItemInfo}"/>
        </g:elseif>

        <g:elseif test="${Tag.isFeedBackTag(tag) || Tag.isTemplateTag(tag)}">
           <qti:hiddenElement xmlNode="${n}" assessmentItemInfo="${assessmentItemInfo}" xmlTag="${tag}"/>
        </g:elseif>

        <g:elseif test="${n.name().getPrefix().equals("m")}">
            <qti:mathML xmlNode="${n}" templateValues="${assessmentItemInfo.templateValues}"/>
        </g:elseif>

    </g:else>

</g:each>
