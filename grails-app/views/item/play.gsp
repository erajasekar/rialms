<%--
  Created by IntelliJ IDEA.
  User: Rajasekar Elango
  Date: 3/11/12
  Time: 10:43 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html xmlns:m="http://www.w3.org/1998/Math/MathML">
<head>
    <script type="text/javascript"
            src="http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML">
    </script>
    <title>${assessmentItemInfo.title}</title>
</head>

<body>

<h2>${assessmentItemInfo.title}</h2>

<g:if test="${flash.validationErrors}" >
    <g:render template="/renderer/renderValidationErrors" model="[validationErrors:flash.validationErrors]"/>
</g:if>

<g:else>
    <g:form name="AssessmentItemForm" action="play">
        <g:render template="/renderer/renderItemSubTree"
                  model="[node: assessmentItemInfo.xmlRoot, assessmentItemInfo: assessmentItemInfo]"/>

        <g:submitButton value="Submit" name="submit"/>
    </g:form>
</g:else>

<g:if test="${params.showInternalState}">
    <g:render template="/renderer/renderInternalState" model="[outcomeValues:assessmentItemInfo.outcomeValues]" />
</g:if>
</body>
</html>