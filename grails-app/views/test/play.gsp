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



    <g:formRemote name="AssessmentForm" url="[action: AssessmentItemInfo.controllerActionForProcessItem]"
                  onSuccess="${AssessmentItemInfo.onSuccessCallbackForProcessItem}">

        <g:render template="/renderer/renderAssessmentItem"/>

        <g:render template="/renderer/renderTestFeedback"/>

        <hr/>

        <% NavigationControls controls = assessmentParams[Consts.navigationControls] %>

        <g:each in="${controls.getButtonStates()}" var="button">
            <g:if test="${button.value}">

                <button id="${button.key.id}" type="button" class="btn btn-primary"
                        onclick="${remoteFunction(action: 'navigate', onSuccess: AssessmentItemInfo.onSuccessCallbackForProcessItem, params: params + [(Consts.navButton): button.key.id])}">
                    <i class="${button.key.iconClass}"></i>${button.key.value}
                </button>

            </g:if>
            <g:else>

                <button id="${button.key.id}" style="display:none" type="button" class="btn btn-primary"
                        onclick="${remoteFunction(action: 'navigate', onSuccess: AssessmentItemInfo.onSuccessCallbackForProcessItem, params: params + [(Consts.navButton): button.key.id])}">
                    <i class="${button.key.iconClass}"></i>${button.key.value}
                </button>

            </g:else>
        </g:each>

        <!-- TODO: Internalize all button labels -->
        <g:link action="report" params="[id: params.id]" class="btn btn-primary"><i
                class="icon-signal icon-white"></i> Report</g:link>

        <g:link name='exit' action="reset" params="[id: params.id, redirectto: 'list']" class="btn btn-danger"
                onclick="return confirm(\'${g.message(code: 'test.exit.confirm.message')}\')"><i
                class="icon-remove icon-white"></i> Exit Test</g:link>

    </g:formRemote>

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