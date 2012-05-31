<%--
  Created by IntelliJ IDEA.
  User: relango
  Date: 3/30/12
  Time: 12:19 AM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.rialms.consts.Constants as Consts" %>
<html>
<head>
    <meta name="layout" content="primary"/>
    <title>${testReport.testTitle}</title>
</head>

<body>

<g:render template="/renderer/renderTestTitle"
          model="[testTitle: g.message(code: 'test.report.title.message', args: [testReport.testTitle])]"/>
<div class="row-fluid">
    <div class="span2">&nbsp;</div>

    <div class="span8">
        <div>
            <h3 class="title"><g:message code="test.report.summary.message"/></h3>
            <g:render template="/renderer/renderMapAsTable" model="[mapTableData: testReport.summary]"/>
        </div>

        <h3 class="title"><g:message code="test.report.detail.message"/></h3>
        <div>
            <g:if test="${testReport.detail.isEmpty()}">
                <g:message code="test.report.noreportdetail.error"/>
            </g:if>
            <g:else>
                <table class="table table-bordered table-stripped">
                    <thead>
                    <tr>
                        <g:each var='columnNames' in="${testReport.detail[0].keySet()}">
                            <th>${columnNames}</th>
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
        </div>
    </div>
</div>

</body>
</html>