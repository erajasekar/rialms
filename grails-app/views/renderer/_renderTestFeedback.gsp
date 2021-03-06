<%@ page import="com.rialms.consts.Constants as Consts" %>
<%--
  Created by IntelliJ IDEA.
  User: Rajasekar Elango
  Date: 04/16/12
  Time: 11:03 PM
  To change this template use File | Settings | File Templates.
--%>
<div id='testFeedback'>
    <g:if test="${assessmentParams[Consts.assessmentFeedback] || assessmentParams[Consts.testPartFeedback]}">
        <% String feedbackClass = ""%>
        <g:if test="${includeHeader}" >
            <div class="block-header">
                <h4>
                    <g:message code="test.feedback.message"/>
                </h4>
            </div>
            <% feedbackClass = "block-content" %>
        </g:if>

        <div class="${feedbackClass}">
            <g:if test="${assessmentParams[Consts.assessmentFeedback]}">

                <g:each var="assessmentFeedback" in="${assessmentParams[Consts.assessmentFeedback].children()}">
                    <g:render template="/renderer/renderItemSubTree"
                              model="[node: assessmentFeedback, assessmentParams: assessmentParams]"/>
                </g:each>
            </g:if>
            <g:if test="${assessmentParams[Consts.testPartFeedback]}">
                <g:each var="testPartFeedback" in="${assessmentParams[Consts.testPartFeedback].children()}">
                    <g:render template="/renderer/renderItemSubTree"
                              model="[node: testPartFeedback, assessmentParams: assessmentParams]"/>
                </g:each>
            </g:if>
        </div>
    </g:if>
    <g:else>
        <g:if test="${params.action == 'feedback'}">
            <g:message code="test.nofeedback.message"/>
        </g:if>
    </g:else>

</div>