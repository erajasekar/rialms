<%@ page import="com.rialms.assessment.item.AssessmentItemInfo; com.rialms.consts.Constants as Consts" %>
<%--
  Created by IntelliJ IDEA.
  User: Rajasekar Elango
  Date: 5/30/12
  Time: 11:55 PM
  To change this template use File | Settings | File Templates.
--%>
<div class="block-header">
    <h4>${assessmentTitle} <span class="pull-right">
        <g:if test="${params.controller == 'test'}">
            <g:link action="report" params="[id: params.id]" rel="tooltip"
                    title="${message(code: 'button.report.label')}">
                <i class="icon-signal"></i></g:link>
        </g:if>
        <g:link name='exit' action="reset" params="[id: params.id, redirectto: 'list']"  rel="tooltip"
                onclick="return confirm(\'${g.message(code: 'test.exit.confirm.message')}\')"
                title="${message(code: 'button.exitTest.label')}">
            <i class="icon-remove"></i>
        </g:link></span></h4>
</div>