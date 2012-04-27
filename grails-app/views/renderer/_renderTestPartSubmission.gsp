<%@ page import="com.rialms.assessment.item.AssessmentItemInfo" %>
<%--
  Created by IntelliJ IDEA.
  User: relango
  Date: 4/26/12
  Time: 12:37 AM
  To change this template use File | Settings | File Templates.
--%>
<!-- TODO Include test part name in message -->
<h4>
    <g:if test="${assessmentParams.testPart}">
        <g:message code="testpart.submission.confirm.message" args="${[assessmentParams.testPart]}"/>
    </g:if>
    <g:else>
        <g:message code="test.submission.confirm.message"/>
    </g:else>
</h4>

<g:render template="/renderer/renderMapAsTable" model="[mapTableData: assessmentParams.itemsPendingSubmission]"/>

<g:hiddenField name="id" value="${params.id}"/>
<g:submitToRemote id='submit'
                  value='Submit'
                  url="[action: 'submitTestPart']"
                  name='submit'
                  onSuccess="${AssessmentItemInfo.onSuccessCallbackForProcessItem}"/>

