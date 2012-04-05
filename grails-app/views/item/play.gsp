<%--
  Created by IntelliJ IDEA.
  User: Rajasekar Elango
  Date: 3/11/12
  Time: 10:43 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page import="com.rialms.assessment.item.AssessmentItemInfo" contentType="text/html;charset=UTF-8" %>
<html xmlns:m="http://www.w3.org/1998/Math/MathML">
<head>

    <meta name="layout" content="primary"/>
    <title>${assessmentItemInfo.title}</title>
    <r:require module="core"/>
    <r:script>
        /*function updateRenderedItem(data) {

         if (data.isComplete) {
         $('#submit').attr("disabled", true);
         }
         for (var i = 0; i < data.visibleElementIds.length; i++) {
         $(data.visibleElementIds[i]).show();
         }
         for (var i = 0; i < data.hiddenElementIds.length; i++) {
         $(data.hiddenElementIds[i]).hide();
         }
         var outcomeValuesText = JSON.stringify(data.outcomeValues);
         $('#itemOutcomeValues').text(outcomeValuesText);
         }   */

    </r:script>

</head>

<body>

<h2>${assessmentItemInfo.title}</h2>

<g:if test="${flash.validationErrors}">
    <g:render template="/renderer/renderValidationErrors" model="[validationErrors: flash.validationErrors]"/>
</g:if>

<g:else>
    <div id='message'></div>

    <div id='error'></div>
    <g:form name="AssessmentItemForm" action="${AssessmentItemInfo.controllerActionForProcessItem}">
        <g:render template="/renderer/renderItemSubTree"
                  model="[node: assessmentItemInfo.xmlRoot, assessmentItemInfo: assessmentItemInfo]"/>

        <g:submitToRemote id='submit' value='Submit' url="[action: AssessmentItemInfo.controllerActionForProcessItem]"
                          name='submit'
                          onSuccess="updateRenderedItem(data)"/>
    </g:form>
</g:else>

<g:if test="${params.showInternalState}">
    <g:render template="/renderer/renderInternalState"
              model="[outcomeValues: assessmentItemInfo.outcomeValues, divId: 'itemOutcomeValues']"/>
</g:if>
</body>
</html>