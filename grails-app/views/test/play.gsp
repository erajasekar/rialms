<%--
  Created by IntelliJ IDEA.
  User: Rajasekar Elango
  Date: 3/23/12
  Time: 11:48 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page import="org.qtitools.qti.validation.ValidationItem" contentType="text/html;charset=UTF-8" %>
<html xmlns:m="http://www.w3.org/1998/Math/MathML">
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

<g:if test="${assessmentParams.validationErrors}">
    <g:render template="/renderer/renderValidationErrors"
              model="[validationErrors: assessmentParams.validationErrors]"/>
</g:if>

<g:else>

    <% List<ValidationItem> validationErrors = assessmentItemInfo.validate() %>

    <g:if test="${!validationErrors.isEmpty()}">
        <g:render template="/renderer/renderValidationErrors"
                  model="[validationErrors: validationErrors]"/>

    </g:if>
    <g:else>
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
    </g:else>
</g:else>
<g:if test="${params.showInternalState}">
    <g:render template="/renderer/renderInternalState" model="[outcomeValues:assessmentItemInfo.outcomeValues]" />
    <g:render template="/renderer/renderInternalState" model="[outcomeValues:assessmentParams.outcomeValues]" />
</g:if>
</body>
</html>