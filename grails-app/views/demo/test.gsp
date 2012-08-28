<%--
  Created by IntelliJ IDEA.
  User: relango
  Date: 7/22/12
  Time: 11:24 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page import="com.rialms.assessment.test.Test; com.rialms.assessment.test.Test; com.rialms.assessment.test.TestFeature" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="primary"/>
    <title><g:message code="test.list.title"/></title>
</head>

<body>
<div class="row-fluid">
    <div class="span3">
        <g:render template="/renderer/renderFeatures" model="[features:TestFeature.getFeatures()]"/>
    </div>

    <div class="span9">
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

                    <g:sortableColumn property="id" title="${g.message(code: 'id.label')}"  params="${params}"/>

                    <g:sortableColumn property="title" title="${g.message(code: 'title.label')}"  params="${params}"/>

                    <th><g:message code="action.label"/></th>

                </tr>
                </thead>
                <tbody>
                <g:each in="${testList}" status="i" var="test">
                    <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

                        <td>${fieldValue(bean: test, field: 'id')}</td>

                        <td>${fieldValue(bean: test, field: 'title')}
                            <g:each in="${test.features}" var="feature">
                                <span class='feature-list'>
                                    <code>
                                        <g:link title="${feature.description}" action="${params.action}"
                                                params="${params + ['filterByFeature': feature.name]}">
                                            ${feature.name}
                                        </g:link>
                                    </code>
                                </span>
                            </g:each>
                        </td>

                        <td>
                            <g:link class="btn btn-info" controller="${params.action}" action="play" id="${test.id}" target="_blank">
                                <i class="icon-play">&nbsp;</i><g:message code="play.label"/>
                            </g:link>
                        </td>

                    </tr>
                </g:each>
                </tbody>
            </table>
        </div>

        <b:paginate total="${testList.getTotalCount()}" params="${params}"/>
    </div>
</div>
</body>
</html>
