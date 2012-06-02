<%@ page import="com.rialms.assessment.item.AssessmentItemInfo; com.rialms.consts.Constants as Consts" %>
<%--
  Created by IntelliJ IDEA.
  User: Rajasekar Elango
  Date: 5/30/12
  Time: 11:55 PM
  To change this template use File | Settings | File Templates.
--%>
<div class="block-header">
    <% boolean isTest = params.controller == 'test' %>
    <h4>${assessmentTitle} <span class="pull-right">
        <g:if test="${isTest}">
            <g:link action="report" params="[id: params.id]" rel="tooltip"
                    title="${message(code: 'button.report.label')}">
                <i class="icon-bar-chart"></i>
            </g:link>
            <g:link name='restart' action="reset" params="[id: params.id]" rel="tooltip"
                    onclick="return confirm(\'${g.message(code: 'test.restart.confirm.message')}\')"
                    title="${message(code: 'button.restartTest.label')}">
                <i class="icon-refresh"></i>
            </g:link>
        </g:if>
        <g:link name='exit' action="reset" params="[id: params.id, redirectto: 'list']" rel="tooltip"
                onclick="return confirm(\'${g.message(code: 'test.exit.confirm.message')}\')"
                title="${message(code: isTest ? 'button.exitTest.label' : 'button.exitItem.label')}">
            <i class="icon-remove"></i>
        </g:link></span></h4>
</div>