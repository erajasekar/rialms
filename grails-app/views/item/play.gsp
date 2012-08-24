<%--
  Created by IntelliJ IDEA.
  User: Rajasekar Elango
  Date: 3/11/12
  Time: 10:43 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page import="com.rialms.angular.JsObjectUtil; com.rialms.consts.Constants as Consts; com.rialms.assessment.item.AssessmentItemInfo" contentType="text/html;charset=UTF-8" %>
<html xmlns:m="http://www.w3.org/1998/Math/MathML">
<head>

    <meta name="layout" content="primary"/>
    <title>${assessmentItemInfo[Consts.title]}</title>
</head>

<body>

<r:script>
    $(document).ready(function () {
        initTestRendering();
        initAngularScopeObjects(${JsObjectUtil.createJSONObject(Consts.angularData, [(Consts.itemStylesheets): assessmentItemInfo.itemStylesheets, (Consts.isResponseValid):assessmentItemInfo.isResponseValid])});
    });
</r:script>


<div class="row-fluid">

    <div class="span12" id="${Consts.content}" ng-controller='ItemContentController'>

        <g:if test="${!flash[Consts.validationResult].allItems.isEmpty()}">
            <g:render template="/renderer/renderValidationErrors"
                      model="[validationResult: flash[Consts.validationResult]]"/>
        </g:if>

        <g:if test="${flash[Consts.validationResult].getErrors().isEmpty()}">
            <g:formRemote name="AssessmentItemForm"
                          url="[action: AssessmentItemInfo.controllerActionForProcessItem]"
                          onSuccess="${AssessmentItemInfo.onSuccessCallbackForProcessItem}">
                <g:render template="/renderer/renderAssessmentHeader"
                          model="[(Consts.assessmentTitle): assessmentItemInfo[Consts.title]]"/>
                <div class="block-content">

                    <g:render template="/renderer/renderItemSubTree"
                              model="[node: assessmentItemInfo.xmlRoot, assessmentItemInfo: assessmentItemInfo]"/>
                    <hr/>

                            <qti:submit assessmentItemInfo="${assessmentItemInfo}"
                                        value='${g.message(code: "button.submit.label")}'
                                        name="${Consts.submit}"/>
                            <qti:endAttemptButtons assessmentItemInfo="${assessmentItemInfo}"/>

                            <span ng-bind-html="${Consts.itemResult}">
                                <qti:itemResult assessmentItemInfo="${assessmentItemInfo}"/>
                            </span>
                            <g:render template="/renderer/renderResponseValidation"/>
                            <qti:helpButtons assessmentItemInfo="${assessmentItemInfo}"/>
                </div>
            </g:formRemote>
        </g:if>

        <g:if test="${params[Consts.showInternalState]}">
            <g:render template="/renderer/renderInternalState"
                      model="[outcomeValues: assessmentItemInfo[Consts.outcomeValues], divId: Consts.itemOutcomeValues]"/>
        </g:if>
    </div>
</div>

</body>
</html>