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
        <meta name="layout" content="main" />
        <title><g:message code="test.list.title" /></title>
    </head>
    <body>
        <div class="body">
            <h1><g:message code="test.list.title" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                 	        <g:sortableColumn property="id" title="Id" />

                            <g:sortableColumn property="title" title="Title" />

                   	        <g:sortableColumn property="dataFile" title="Data File" />

                   	        <td>Action</td>

                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${testList}" status="i" var="test">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

                            <td>${fieldValue(bean:test, field:'id')}</td>

                            <td>${fieldValue(bean:test, field:'title')}</td>

                            <td>${fieldValue(bean:test, field:'dataFile')}</td>

                            <td>
								<g:link action="play" id="${test.id}">play</g:link>
							</td>

                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${com.rialms.assessment.test.Test.count()}" />
            </div>
        </div>
    </body>
</html>
