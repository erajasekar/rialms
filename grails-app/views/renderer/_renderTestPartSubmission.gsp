<%@ page import="com.rialms.assessment.item.AssessmentItemInfo" %>
<%--
  Created by IntelliJ IDEA.
  User: relango
  Date: 4/26/12
  Time: 12:37 AM
  To change this template use File | Settings | File Templates.
--%>
<!-- TODO Include test part name in message -->
<h4><g:message code="test.submission.confirm.message"/></h4>

<g:render template="/renderer/renderMapAsTable" model="[mapTableData: assessmentParams.itemsPendingSubmission]"/>

<g:hiddenField name="id" value="${params.id}"/>
<g:submitToRemote id='submit'
                  value='Submit'
                  url="[action: 'submitTestPart']"
                  name='submit'
                  onSuccess="${AssessmentItemInfo.onSuccessCallbackForProcessItem}"/>

