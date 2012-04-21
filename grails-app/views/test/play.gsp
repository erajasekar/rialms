<%--
  Created by IntelliJ IDEA.
  User: Rajasekar Elango
  Date: 3/23/12
  Time: 11:48 PM
  To change this template use File | Settings | File Templates.
--%>
<!doctype html>
<%@ page import="com.rialms.consts.NavButton; com.rialms.assessment.test.NavigationControls; com.rialms.assessment.item.AssessmentItemInfo; org.qtitools.qti.validation.ValidationResult" contentType="text/html;charset=UTF-8" %>
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



    <g:form name="AssessmentForm" action="play">

        <g:render template="/renderer/renderTestContent"/>

        <g:render template="/renderer/renderTestFeedback"/>

        <hr/>

        <% NavigationControls controls = assessmentParams.navigationControls %>

        <g:each in="${controls.getButtonStates()}" var="button">
            <g:if test="${!button.equals(NavButton.submit)}">
                <g:if test="${button.value}">
                    <g:submitButton id="${button.key.id}"
                                    name="${button.key.name}"
                                    value="${button.key.value}"
                                    onclick="${remoteFunction(action: 'navigate', onSuccess: AssessmentItemInfo.onSuccessCallbackForProcessItem)}"/>

                </g:if>
                <g:else>
                    <g:submitButton id="${button.key.id}"
                                    name="${button.key.name}"
                                    value="${button.key.value}"
                                    onclick="${remoteFunction(action: 'navigate', onSuccess: AssessmentItemInfo.onSuccessCallbackForProcessItem)}"
                                    style="display:none"/>
                </g:else>
            </g:if>
        </g:each>

        <g:submitButton name="report" value="Report"/>
        <!-- TODO ADD CONFIRMATION TO MESSAGE.PROPERTIES -->
        <g:submitButton name="exit" value="Exit Test"
                        onclick="return confirm('Are you sure you want to end this test? All progress will be lost.')"/>

    </g:form>

</g:if>
<g:if test="${params.showInternalState}">
    <g:render template="/renderer/renderInternalState"
              model="[outcomeValues: assessmentItemInfo.outcomeValues, divId: 'itemOutcomeValues']"/>
    <g:render template="/renderer/renderInternalState"
              model="[outcomeValues: assessmentParams.outcomeValues, divId: 'testOutcomeValues']"/>
    <br/>
    ${assessmentParams.testStatus}
</g:if>
</body>
</html>