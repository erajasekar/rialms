<%--
  Created by IntelliJ IDEA.
  User: Rajasekar Elango
  Date: 3/23/12
  Time: 11:48 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <script type="text/javascript"
            src="http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML">
    </script> <!-- TODO : This has to be test title -->
    <title>${assessmentItemInfo.title}</title>
</head>

<body>
<g:form name="AssessmentForm" action="play">
    <g:render template="/renderer/renderItemSubTree"
              model="[node: assessmentItemInfo.xmlRoot, assessmentItemInfo: assessmentItemInfo]"/>

    <g:hiddenField name="id" value="${params.id}"/>

    <g:submitButton value="Enter" name="processButton"/>

    <g:if test="${assessmentParams.nextEnabled}">
        <g:submitButton name="next" value="Next"/>
    </g:if>
</g:form>

<g:if test="${assessmentItemInfo.outcomeValues}">
    ---------------------------------
    <br/>
    ${assessmentItemInfo.outcomeValues}
</g:if>
</body>
</html>