<%--
  Created by IntelliJ IDEA.
  User: Rajasekar Elango
  Date: 3/23/12
  Time: 11:48 PM
  To change this template use File | Settings | File Templates.
--%>
<!doctype html>
<%@ page import="com.rialms.assessment.test.NavigationControls; com.rialms.assessment.item.AssessmentItemInfo; org.qtitools.qti.validation.ValidationResult" contentType="text/html;charset=UTF-8" %>
<html xmlns:m="http://www.w3.org/1998/Math/MathML">
<head>
    <meta name="layout" content="primary"/>
    <title>${assessmentParams.title}</title>
</head>

<body>

<g:if test="${flash.message}">
    <g:message code="${flash.message}"/>
</g:if>

<h2>${assessmentParams.title}</h2>

<g:if test="${!assessmentParams.validationResult.allItems.isEmpty()}">
    <g:render template="/renderer/renderValidationErrors"
              model="[validationErrors: assessmentParams.validationResult.allItems]"/>
</g:if>

<g:if test="${assessmentParams.validationResult.errors.isEmpty()}">

    <g:if test="${assessmentParams.timeRemaining > 0}">
        <r:script>
                $(document).ready(function(){
                   initTimer("${assessmentParams.timeRemaining}")
                });
        </r:script>

        <div id="timer">
            <g:message code="test.time.to.complete.message"/>
            <b id="timeRemaining">...</b>
        </div>
    </g:if>

    <h3>${assessmentItemInfo.title}</h3>

    <qti:assessmentSection sectionTitles="${assessmentParams.sectionTitles}"/>
    <hr/>

    <g:if test="${assessmentParams.rubric}">

        <g:each var="section" in="${assessmentParams.rubric.children()}">
            <g:render template="/renderer/renderItemSubTree" model="[node: section.children().get(0)]"/>
        </g:each>

    </g:if>

    <g:form name="AssessmentForm" action="play">
        <g:render template="/renderer/renderItemSubTree"
                  model="[node: assessmentItemInfo.xmlRoot, assessmentItemInfo: assessmentItemInfo]"/>

        <g:hiddenField name="id" value="${params.id}"/>
        <g:hiddenField name="questionId" value="${assessmentParams.questionId}"/>

        <g:if test="${assessmentParams.submitEnabled}">
            <qti:submit assessmentItemInfo="${assessmentItemInfo}" value='Submit'
                        url="[action: AssessmentItemInfo.controllerActionForProcessItem]"
                        name='submit'
                        onSuccess="${AssessmentItemInfo.onSuccessCallbackForProcessItem}"/>
        </g:if>
        <g:else>
            <g:submitButton value="Submit" name="submit" disabled="disabled"/>
        </g:else>

        <g:render template="/renderer/renderTestFeedback"/>

        <hr/>

        <% NavigationControls controls = assessmentParams.navigationControls %>

        <g:each in="${controls.getButtonStates()}" var="button">
            <g:if test="${button.value}">
                <g:submitButton id="${button.key.id}" name="${button.key.name}" value="${button.key.value}"/>
            </g:if>
            <g:else>
                <g:submitButton id="${button.key.id}" name="${button.key.name}" value="${button.key.value}"
                                style="display:none"/>
            </g:else>
        </g:each>

        <g:submitButton name="report" value="Report"/>
        <g:submitButton name="exit" value="Exit Test"
                        onclick="return confirm('Are you sure you want to end this test? All progress will be lost.')"/>

    </g:form>

</g:if>
<g:if test="${params.showInternalState}">
    <g:render template="/renderer/renderInternalState" model="[outcomeValues: assessmentItemInfo.outcomeValues]"/>
    <g:render template="/renderer/renderInternalState" model="[outcomeValues: assessmentParams.outcomeValues]"/>
    <br/>
    ${assessmentParams.testStatus}
</g:if>
</body>
</html>