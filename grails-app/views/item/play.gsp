<%--
  Created by IntelliJ IDEA.
  User: Rajasekar Elango
  Date: 3/11/12
  Time: 10:43 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page import="com.rialms.consts.Constants as Consts; com.rialms.assessment.item.AssessmentItemInfo" contentType="text/html;charset=UTF-8" %>
<html xmlns:m="http://www.w3.org/1998/Math/MathML">
<head>

    <meta name="layout" content="primary"/>
    <title>${assessmentItemInfo[Consts.title]}</title>
</head>

<body>

<div class="row-fluid">
    <div class="span3">&nbsp;</div> <!-- Place holder for navigation -->
    <div class="span8">
        <div class="well">
            <h4>${assessmentItemInfo[Consts.title]}</h4>
        </div>
        <g:if test="${!flash[Consts.validationResult].allItems.isEmpty()}">
            <g:render template="/renderer/renderValidationErrors"
                      model="[validationErrors: flash[Consts.validationResult].allItems]"/>
        </g:if>

        <g:if test="${flash[Consts.validationResult].getErrors().isEmpty()}">
            <div id='message'></div>

            <div id='error'></div>
            <g:form name="AssessmentItemForm" action="${AssessmentItemInfo.controllerActionForProcessItem}">
                <g:render template="/renderer/renderItemSubTree"
                          model="[node: assessmentItemInfo.xmlRoot, assessmentItemInfo: assessmentItemInfo]"/>

                <qti:submit assessmentItemInfo="${assessmentItemInfo}" value='Submit'
                            url="[action: AssessmentItemInfo.controllerActionForProcessItem]"
                            name="${Consts.submit}"
                            onSuccess="${AssessmentItemInfo.onSuccessCallbackForProcessItem}"/>
            </g:form>
        </g:if>

        <g:if test="${params[Consts.showInternalState]}">
            <g:render template="/renderer/renderInternalState"
                      model="[outcomeValues: assessmentItemInfo[Consts.outcomeValues], divId: Consts.itemOutcomeValues]"/>
        </g:if>
    </div>
</div>

</body>
</html>