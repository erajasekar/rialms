<%--
  Created by IntelliJ IDEA.
  User: relango
  Date: 3/30/12
  Time: 12:19 AM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>

    </title>
</head>

<body>

<h2><g:message code="test.report.title.message" args="${[testReport.testTitle]}"/></h2>

<h2><g:message code="test.report.summary.message"/></h2>

<table>
    <g:each var='summary' in="${testReport.summary}">
        <tr>
            <td>${summary.key}</td>
            <td>${summary.value}</td>
        </tr>
    </g:each>
</table>

<h2><g:message code="test.report.detail.message"/></h2>

<g:if test="${testReport.detail.isEmpty()}">
    <g:message code="test.report.noreportdetail.error"/>
</g:if>
<g:else>
    <table>
        <thead>
        <tr>
            <g:each var='columnNames' in="${testReport.detail[0].keySet()}">
                <td>${columnNames}</td>
            </g:each>
        </tr>
        </thead>
        <g:each var='detail' in="${testReport.detail}">
            <tr>
                <g:each in="${detail}" var="columnValues">
                    <td>${columnValues.value}</td>
                </g:each>
            </tr>
        </g:each>
    </table>
</g:else>

</body>
</html>