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
    <title>${testReport.testTitle}</title>
</head>

<body>
<h2>Summary</h2>

<table>
    <g:each var='summary' in="${testReport.summary}">
        <tr>
            <td>${summary.key}</td>
            <td>${summary.value}</td>
        </tr>
    </g:each>
</table>

<h2>Detail</h2>

<g:if test="${testReport.detail.isEmpty()}">
    No Report Detail Found.
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