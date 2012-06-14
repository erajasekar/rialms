<%--
  Created by IntelliJ IDEA.
  User: Rajasekar Elango
  Date: 3/23/12
  Time: 11:48 PM
  To change this template use File | Settings | File Templates.
--%>
<!doctype html>
<%@ page import="com.rialms.angular.JsObjectUtil; grails.converters.JSON; com.rialms.assessment.test.NavigationControls; com.rialms.assessment.item.AssessmentItemInfo; org.qtitools.qti.validation.ValidationResult; com.rialms.consts.Constants as Consts" contentType="text/html;charset=UTF-8" %>
<html xmlns:m="http://www.w3.org/1998/Math/MathML">
<head>
    <meta name="layout" content="primary"/>
    <title>${assessmentParams[Consts.title]}</title>
</head>

<body>

<r:script>
    $(document).ready(function () {
        initTestRendering();
        initTestStatusModel(${assessmentParams[Consts.testStatusModel] as JSON});
        initNavigationButtonStates(${assessmentParams[Consts.navigationButtonStates] as JSON});
    });
</r:script>

<g:render template="/renderer/renderTestTitle" model="[testTitle: assessmentParams[Consts.title]]"/>

<div class="row-fluid">
    <div class="span3">
        <a href="#" rel="tooltip" class="toggleNav pull-right" title="Hide Sidebar"><span>&laquo;</span></a>
    </div>
</div>

<div class="row-fluid">

    <g:render template="/renderer/renderTestPartStatus" model="[assessmentParams: assessmentParams]"/>

    <div class="span9" id="content">
        <g:if test="${flash.message}">
            <g:message code="${flash.message}"/>
        </g:if>

        <g:if test="${!assessmentParams.validationResult.allItems.isEmpty()}">
            <g:render template="/renderer/renderValidationErrors"
                      model="[validationErrors: assessmentParams[Consts.validationResult].allItems]"/>
        </g:if>

        <g:if test="${assessmentParams[Consts.validationResult].errors.isEmpty()}">

            <g:if test="${assessmentParams[Consts.timeRemaining] > 0}">
                <r:script disposition='head'>
                $(document).ready(function(){
                   initTimer("${assessmentParams[Consts.timeRemaining]}");
                });
                </r:script>
                <div class="row-fluid">
                    <div class="span4"></div>

                    <div id="timer" class="span4 well-small center">
                        <h3>
                            <g:message code="test.time.to.complete.message"/>
                            <b id="${Consts.timeRemaining}">...</b>
                        </h3>
                    </div>

                    <div class="span4"></div>
                </div>
            </g:if>

            <g:formRemote name="AssessmentForm"
                          url="[action: AssessmentItemInfo.controllerActionForProcessItem]"
                          onSuccess="${AssessmentItemInfo.onSuccessCallbackForProcessItem}">

                <div id="AssessmentForm">
                    <g:render template="/renderer/renderAssessmentHeader"
                              model="[assessmentTitle: assessmentItemInfo.title]"/>
                    <g:render template="/renderer/renderAssessmentItem"/>
                    <g:render template="/renderer/renderTestFeedback"/>
                    <div class="form-actions" id="${Consts.navigationControls}">
                        <%
                            NavigationControls controls = assessmentParams[Consts.navigationControls]
                            JsObjectUtil.PropertyConstructor buttonStates = new JsObjectUtil.PropertyConstructor(Consts.navigationButtonStates)
                        %>

                        <g:each in="${controls.getButtonStates()}" var="button">

                            <button ng-show="${buttonStates.getProperty(button.key.name)}" id="${button.key.id}"
                                    type="button" class="btn btn-info"
                                    onclick="${remoteFunction(action: 'navigate', onSuccess: AssessmentItemInfo.onSuccessCallbackForProcessItem, params: params + [(Consts.navButton): button.key.id])}">
                                <g:if test="${button.key.appendIcon}">
                                    ${button.key.value}&nbsp;<i class="${button.key.iconClass}"></i>
                                </g:if>
                                <g:else>
                                    <i class="${button.key.iconClass}"></i>&nbsp;${button.key.value}
                                </g:else>

                            </button>

                        </g:each>
                    </div>
                </div>
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
    </div>
</div>

</body>
</html>