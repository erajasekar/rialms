<%@ page import="grails.converters.JSON; com.rialms.assessment.item.Item; com.rialms.assessment.item.ItemFeature" %>
<%--
  Created by IntelliJ IDEA.
  User: relango
  Date: 7/22/12
  Time: 11:24 PM
  To change this template use File | Settings | File Templates.
--%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="primary"/>
    <r:require module="prettify"/>
    <title><g:message code="item.list.title"/></title>
</head>

<body>
<div class="row-fluid">
    <div class="span3">
        <g:render template="/renderer/renderFeatures" model="[features: ItemFeature.getFeatures()]"/>
    </div>

    <div class="span9">

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
                    <g:sortableColumn property="id" title="${g.message(code: 'id.label')}" params="${params}"/>
                    <th><g:message code="view.label"/> </th>
                    <g:sortableColumn property="title" title="${g.message(code: 'title.label')}" params="${params}"/>

                    <th><g:message code="action.label"/></th>

                </tr>
                </thead>
                <tbody>
                <g:each in="${itemList}" status="i" var="item">
                    <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

                        <td>${fieldValue(bean: item, field: 'id')}
                        <td>
                            <button class="btn btn-info" onclick="window.showUrlInDialog('/rialms/viewItemXML/${item.id}')">
                               <g:message code="view.label"/>
                            </button>
                        </td>
                        <td>${fieldValue(bean: item, field: 'title')}
                            <g:each in="${item.features}" var="feature">
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
                            <g:link class="btn btn-info" controller="${params.action}" action="play" id="${item.id}" target="_blank">
                                <i class="icon-play">&nbsp;</i><g:message code="play.label"/>
                            </g:link>
                        </td>

                    </tr>
                </g:each>
                </tbody>
            </table>
        </div>
        <b:paginate total="${itemList.getTotalCount()}" params="${params}"/>
    </div>
</div>

</body>
</html>
