<%@ page import="com.rialms.assessment.Item; com.rialms.assessment.item.Item" %>
<%--
  Created by IntelliJ IDEA.
  User: relango
  Date: 3/22/12
  Time: 10:59 PM
  To change this template use File | Settings | File Templates.
--%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="primary"/>
    <title><g:message code="item.list.title"/></title>
</head>

<body>
<div class="row-fluid">
    <div class="span2">&nbsp;</div>

    <div class="span8">

        <div class="title">
            <h1><g:message code="item.list.title"/></h1>
        </div>
        <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
        </g:if>
        <div>
            <table class="table table-stripped">
                <thead>
                <tr>
                    <g:sortableColumn property="id" title="Id"/>

                    <g:sortableColumn property="title" title="title"/>

                    <th>Action</th>

                </tr>
                </thead>
                <tbody>
                <g:each in="${itemList}" status="i" var="item">
                    <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

                        <td>${fieldValue(bean: item, field: 'id')}</td>

                        <td>${fieldValue(bean: item, field: 'title')}</td>

                        <td>
                            <g:link class="btn btn-info"  action="play" id="${item.id}" target="_blank">
                                <i class="icon-play">&nbsp;</i><g:message code="play.label" />
                            </g:link>
                        </td>

                    </tr>
                </g:each>
                </tbody>
            </table>
        </div>

        <div class="paginateButtons">
            <g:paginate total="${Item.count()}"/>
        </div>
    </div>
</div>
</body>
</html>
