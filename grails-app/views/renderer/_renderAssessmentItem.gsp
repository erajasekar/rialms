<%@ page import="com.rialms.assessment.item.AssessmentItemInfo" %>
<%--
  Created by IntelliJ IDEA.
  User: Rajasekar Elango
  Date: 4/19/12
  Time: 11:55 PM
  To change this template use File | Settings | File Templates.
--%>
<div id='testContent'>
    <h3>${assessmentItemInfo.title}</h3>

    <g:if test="${assessmentParams.timeRemaining > 0}">
        <script type="text/javascript">
                   initTimer("${assessmentParams.timeRemaining}")
        </script>
    </g:if>

    <qti:assessmentSection sectionTitles="${assessmentParams.sectionTitles}"/>
    <hr/>

    <g:if test="${assessmentParams.rubric}">

        <g:each var="section" in="${assessmentParams.rubric.children()}">
            <g:render template="/renderer/renderItemSubTree" model="[node: section.children().get(0)]"/>
        </g:each>

    </g:if>
    <g:render template="/renderer/renderItemSubTree"
              model="[node: assessmentItemInfo.xmlRoot, assessmentItemInfo: assessmentItemInfo]"/>

    <g:hiddenField name="id" value="${params.id}"/>
    <g:hiddenField name="questionId" value="${assessmentParams.questionId}"/>

    <g:if test="${assessmentParams.submitEnabled}">
        <g:submitToRemote id='submit'
                          value='Submit'
                          url="[action: AssessmentItemInfo.controllerActionForProcessItem]"
                          name='submit'
                          onSuccess="${AssessmentItemInfo.onSuccessCallbackForProcessItem}"/>

    </g:if>
    <g:else>
        <g:submitButton id='submit'
                        value='Submit'
                        url="[action: AssessmentItemInfo.controllerActionForProcessItem]"
                        name='submit'
                        onSuccess="${AssessmentItemInfo.onSuccessCallbackForProcessItem}" disabled="disabled"/>
    </g:else>
</div>