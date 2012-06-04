<%@ page import="com.rialms.consts.Constants; com.rialms.assessment.item.AssessmentItemInfo; com.rialms.consts.Constants as Consts" %>
<%--
  Created by IntelliJ IDEA.
  User: Rajasekar Elango
  Date: 5/30/12
  Time: 11:55 PM
  To change this template use File | Settings | File Templates.
--%>
<div id='assessmentTitle' class="block-header" ng-controller="HeaderController">
    <% boolean isTest = params.controller == 'test' %>
    HINT {{hintButton}}
    <h4>${assessmentTitle}
        <span class="pull-right">
           <g:remoteLink ng-hide="!${Constants.hintJsObj}" rel="tooltip"
                   action="${AssessmentItemInfo.controllerActionForProcessItem}"
                   onSuccess="${AssessmentItemInfo.onSuccessCallbackForProcessItem}"
                   params="['id':'{{'+ Constants.hintJsObj + '.itemId}}',('{{' + Constants.hintJsObj + '.id}}'):'{{' + Constants.hintJsObj +'.title}}']"
                   title="{{hintButton.title}}">
                        <i class="icon-question-sign"></i>
            </g:remoteLink>
            <g:remoteLink ng-hide="!${Constants.solutionJsObj}" rel="tooltip"
                    action="${AssessmentItemInfo.controllerActionForProcessItem}"
                    onSuccess="${AssessmentItemInfo.onSuccessCallbackForProcessItem}"
                    params="['id':'{{'+ Constants.solutionJsObj + '.itemId}}',('{{' + Constants.solutionJsObj + '.id}}'):'{{' + Constants.solutionJsObj +'.title}}']"
                    title="{{solutionButton.title}}">
                         <i class="icon-book"></i>
             </g:remoteLink>
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
            </g:link>
        </span>
    </h4>

</div>