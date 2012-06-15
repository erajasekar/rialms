<%--
  Created by IntelliJ IDEA.
  User: relango
  Date: 4/2/12
  Time: 11:09 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page import="com.rialms.assessment.test.Test; com.rialms.assessment.test.Test" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="primary"/>
    <title><g:message code="test.list.title"/></title>
</head>

<body>
<div class="row-fluid">
    <div class="span2">&nbsp;</div>

    <div class="span8">
        <div class="title">
            <h1><g:message code="test.list.title"/></h1>
        </div>
        <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
        </g:if>
        <div>
            <table class="table table-stripped">
                <thead>
                <tr>

                    <g:sortableColumn property="id" title="${g.message(code: 'id.label')}"/>

                    <g:sortableColumn property="title" title="${g.message(code: 'title.label')}"/>

                    <th>Action</th>

                </tr>
                </thead>
                <tbody>
                <g:each in="${testList}" status="i" var="test">
                    <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

                        <td>${fieldValue(bean: test, field: 'id')}</td>

                        <td>${fieldValue(bean: test, field: 'title')}</td>

                        <td>
                            <g:link class="btn btn-info" action="play" id="${test.id}" target="_blank">
                                <i class="icon-play">&nbsp;</i><g:message code="play.label"/>
                            </g:link>
                        </td>

                    </tr>
                </g:each>
                </tbody>
            </table>
        </div>

        <div class="paginateButtons">
            <g:paginate total="${com.rialms.assessment.test.Test.count()}"/>
        </div>
    </div>
</div>
</body>
</html>
