<%@ page import="com.rialms.assessment.item.ItemFeature; com.rialms.consts.Constants;" %>
<%--
  Created by IntelliJ IDEA.
  User: Rajasekar Elango
  Date: 07/21/12
  Time: 10:48 PM
  To change this template use File | Settings | File Templates.
--%>

<div>
    <div class="block-header">
        <h4>
            <g:message code="features.label"/>
        </h4>
    </div>

    <div class="block-content">
        <div class="feature-list">
            <% String preStyle = params.filterByFeature == 'all' ? "class='active'" : '' %>
            <g:link action="${params.action}" params="${params + ['filterByFeature': 'all']}">
                <pre ${preStyle}><g:message code="showall.label"/></pre>
            </g:link>
        </div>
        <g:each in="${features}" var="feature">
            <% preStyle = params.filterByFeature == feature.name ? "class='active'" : '' %>
            <div class="feature-list">
                <g:link action="${params.action}" params="${params + ['filterByFeature': feature.name, offset:0]}">
                    <pre ${preStyle}><span class="label label-info">${feature.name}</span> ${feature.description}</pre>
                </g:link>
            </div>
        </g:each>
    </div>
</div>
