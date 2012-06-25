<%@ page import="com.rialms.consts.Constants; com.rialms.consts.EndAttemptButton; com.rialms.angular.JsObjectUtil;  com.rialms.assessment.item.AssessmentItemInfo; com.rialms.consts.Constants as Consts" %>
<%--
  Created by IntelliJ IDEA.
  User: Rajasekar Elango
  Date: 5/30/12
  Time: 11:55 PM
  To change this template use File | Settings | File Templates.
--%>
<div id="${Consts.assessmentHeader}" class="block-header">
    <% boolean isTest = params.controller == 'test'
    JsObjectUtil.PropertyConstructor props = new JsObjectUtil.PropertyConstructor(Constants.assessmentHeader)
    %>
    <div ng-init="${props.getProperty(Constants.title)}='${assessmentTitle}'"></div>
    <h4>${props.getPropertyValue(Constants.title)}
        <span class="pull-right">
            <qti:headerButton type='${EndAttemptButton.hint}'/>
            <qti:headerButton type='${EndAttemptButton.solution}'/>
            <g:if test="${isTest}">
                <g:link action="report" params="[id: params.id]" rel="tooltip"
                        title="${message(code: 'button.report.label')}">
                    <i class="icon-bar-chart"></i>
                </g:link>
                <g:link ng-hide="submitDisabled" name='reset' action="reset"
                        params="[id: params.id, (Consts.resetItem): true]" rel="tooltip"
                        title="${message(code: 'button.restartItem.label')}">
                    <i class="icon-repeat"></i>
                </g:link>
            </g:if>
            <g:link name='restart' action="reset" params="[id: params.id]" rel="tooltip"
                    onclick="return confirm(\'${g.message(code: 'test.restart.confirm.message')}\')"
                    title="${message(code: isTest ? 'button.restartTest.label' : 'button.restartItem.label')}">
                <i class="icon-refresh"></i>
            </g:link>

            <g:link name='exit' action="reset" params="[id: params.id, redirectto: 'list']" rel="tooltip"
                    onclick="return confirm(\'${g.message(code: 'test.exit.confirm.message')}\')"
                    title="${message(code: isTest ? 'button.exitTest.label' : 'button.exitItem.label')}">
                <i class="icon-remove"></i>
            </g:link>
        </span>
    </h4>

</div>