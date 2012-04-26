<%--
  Created by IntelliJ IDEA.
  User: relango
  Date: 4/24/12
  Time: 12:37 AM
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

<h4><g:message code="test.submission.confirm.message"/></h4>

<g:form action="confirmSubmission" name="confirmSubmission">

    <g:render template="/renderer/renderMapAsTable" model="[mapTableData: assessmentParams.itemsPendingSubmission]"/>

    <g:hiddenField name="id" value="${params.id}"/>
    <g:submitButton name="submit-test" value="Submit Test"/>

</g:form>

</body>
</html>