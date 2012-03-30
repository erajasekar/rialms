<%--
  Created by IntelliJ IDEA.
  User: relango
  Date: 3/28/12
  Time: 3:17 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page import="groovy.xml.XmlUtil" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>${assessmentParams.title}</title>
</head>

<body>
<h2>${assessmentParams.title}</h2>

<g:if test="${flash.message}">
    <g:message code="${flash.message}"/>
</g:if>

<h4><g:message code="test.complete.message"/></h4>

<g:if test="${assessmentParams.assessmentFeedback}">
    <g:each var="assessmentFeedback" in="${assessmentParams.assessmentFeedback.children()}">
        <g:render template="/renderer/renderItemSubTree"
                  model="[node: assessmentFeedback, assessmentParams: assessmentParams]"/>
    </g:each>
</g:if>
<g:if test="${assessmentParams.testPartFeedback}">
    <g:each var="testPartFeedback" in="${assessmentParams.testPartFeedback.children()}">
        <g:render template="/renderer/renderItemSubTree"
                  model="[node: testPartFeedback, assessmentParams: assessmentParams]"/>
    </g:each>
</g:if>
<br/>
<a href="${g.createLink(action: 'report', params: params)}" target="_blank">
    <g:message code="test.report.view.message"/>
</a>
</body>
</html>