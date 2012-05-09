<%--
  Created by IntelliJ IDEA.
  User: relango
  Date: 3/28/12
  Time: 3:17 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page import="groovy.xml.XmlUtil; com.rialms.consts.Constants as Consts" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>${assessmentParams[Consts.title]}</title>
</head>

<body>
<h2>${assessmentParams[Consts.title]}</h2>

<g:if test="${flash.message}">
    <g:message code="${flash.message}"/>
</g:if>

<h4><g:message code="test.complete.message"/></h4>

<g:render template="/renderer/renderTestFeedback"/>

<a href="${g.createLink(action: 'report', params: params)}" target="_blank">
    <g:message code="test.report.view.message"/>
</a>
<br/>
<a href="${createLink(controller: 'test', action: 'reset')}">
    <g:message code="test.returnto.test.list.message"/>
</a>
</body>
</html>