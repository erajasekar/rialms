<%--
  Created by IntelliJ IDEA.
  User: Rajasekar Elango
  Date: 3/23/12
  Time: 11:48 PM
  To change this template use File | Settings | File Templates.
--%>
<!doctype html>
<%@ page import="com.rialms.assessment.test.NavigationControls; com.rialms.assessment.item.AssessmentItemInfo; org.qtitools.qti.validation.ValidationResult; com.rialms.consts.Constants as Consts" contentType="text/html;charset=UTF-8" %>
<html xmlns:m="http://www.w3.org/1998/Math/MathML">
<head>
    <meta name="layout" content="primary"/>
    <title>${assessmentParams[Consts.title]}</title>
</head>

<body>

<g:if test="${flash.message}">
    <g:message code="${flash.message}"/>
</g:if>

<h2>${assessmentParams[Consts.title]}</h2>

<g:if test="${!assessmentParams.validationResult.allItems.isEmpty()}">
    <g:render template="/renderer/renderValidationErrors"
              model="[validationErrors: assessmentParams[Consts.validationResult].allItems]"/>
</g:if>

<g:if test="${assessmentParams[Consts.validationResult].errors.isEmpty()}">

    <g:if test="${assessmentParams[Consts.timeRemaining] > 0}">
        <r:script disposition='head'>
                $(document).ready(function(){
                   initTimer("${assessmentParams[Consts.timeRemaining]}")
                });
        </r:script>

        <div id="timer">
            <g:message code="test.time.to.complete.message"/>
            <b id="${Consts.timeRemaining}">...</b>
        </div>
    </g:if>



    <g:form name="AssessmentForm" action="play">

        <g:render template="/renderer/renderAssessmentItem"/>

        <g:render template="/renderer/renderTestFeedback"/>

        <hr/>

        <% NavigationControls controls = assessmentParams[Consts.navigationControls] %>

        <g:each in="${controls.getButtonStates()}" var="button">
            <g:if test="${button.value}">
                <input type='button' id="${button.key.id}"
                       name="${button.key.name}"
                       value="${button.key.value}"
                       onclick="${remoteFunction(action: 'navigate', onSuccess: AssessmentItemInfo.onSuccessCallbackForProcessItem, params: params + [(Consts.navButton): button.key.id])}"/>

            </g:if>
            <g:else>
                <input type='button' id="${button.key.id}"
                       name="${button.key.name}"
                       value="${button.key.value}"
                       onclick="${remoteFunction(action: 'navigate', onSuccess: AssessmentItemInfo.onSuccessCallbackForProcessItem, params: params + [(Consts.navButton): button.key.id])}"
                       style="display:none"/>
            </g:else>
        </g:each>

        <g:submitButton name="report" value="Report"/>
        <g:submitButton name="exit" value="Exit Test"
                        onclick="return confirm(\'${g.message(code: 'test.exit.confirm.message')}\')"/>

    </g:form>

</g:if>
<g:if test="${params.showInternalState}">
    <g:render template="/renderer/renderInternalState"
              model="[outcomeValues: assessmentItemInfo[Consts.outcomeValues], divId: Consts.itemOutcomeValues]"/>
    <g:render template="/renderer/renderInternalState"
              model="[outcomeValues: assessmentParams[Consts.outcomeValues], divId: Consts.testOutcomeValues]"/>
    <br/>
    ${assessmentParams[Consts.testStatus]}
</g:if>
</body>
</html>