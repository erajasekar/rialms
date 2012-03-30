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
    </script>
    <title>${assessmentParams.title}</title>
</head>

<body>

<g:if test="${flash.message}">
    <g:message code="${flash.message}"/>
</g:if>

<h2>${assessmentParams.title}</h2>

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
        <g:submitButton value="Submit" name="submit"/>
    </g:if>
    <g:else>
        <g:submitButton value="Submit" name="submit" disabled="disabled"/>
    </g:else>

    <hr/>

    <g:if test="${assessmentParams.nextEnabled}">
        <g:submitButton name="next" value="Next"/>
    </g:if>
    <g:if test="${assessmentParams.previousEnabled}">
        <g:submitButton name="previous" value="Previous"/>
    </g:if>
    <g:if test="${assessmentParams.backwardEnabled}">
        <g:submitButton name="backward" value="Backward"/>
    </g:if>
    <g:if test="${assessmentParams.forwardEnabled}">
        <g:submitButton name="forward" value="Forward"/>
    </g:if>
    <g:if test="${assessmentParams.skipEnabled}">
        <g:submitButton name="skip" value="Skip"/>
    </g:if>
    <g:submitButton name="report" value="Report"/>

</g:form>

<g:if test="${assessmentItemInfo.outcomeValues}">
    ---------------------------------
    <br/>
    ${assessmentItemInfo.outcomeValues}
</g:if>
</body>
</html>