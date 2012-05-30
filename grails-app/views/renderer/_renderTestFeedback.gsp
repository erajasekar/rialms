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
    </g:if>
    <g:else>
        <g:if test="${params.action == 'feedback'}">
            <g:message code="test.nofeedback.message"/>
        </g:if>
    </g:else>

</div>