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

<g:render template="/renderer/renderTestPartSubmission" model="[assessmentParams:assessmentParams]" />
</body>
</html>