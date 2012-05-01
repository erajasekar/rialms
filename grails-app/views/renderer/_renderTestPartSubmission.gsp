<%@ page import="com.rialms.consts.AssessmentItemStatus; com.rialms.assessment.item.AssessmentItemInfo" %>
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

<table>
    <g:each var='entry' in="${assessmentParams.itemsPendingSubmission}">
        <tr>
            <td><!-- Make links to show only if its not timed out -->
            <g:if test="${AssessmentItemStatus.valueOf(entry.value) == AssessmentItemStatus.TIMED_OUT}">
                ${entry.key}
            </g:if>
            <g:else>
                <g:remoteLink action="navigate" onSuccess="${AssessmentItemInfo.onSuccessCallbackForProcessItem}"
                              params="${params + [renderItem: entry.key]}">${entry.key}</g:remoteLink>

            </g:else>
            </td>
            <td>${entry.value}</td>
        </tr>
    </g:each>
</table>

<g:hiddenField name="id" value="${params.id}"/>
<g:submitToRemote id='submit'
                  value='Submit'
                  url="[action: 'submitTestPart']"
                  name='submit'
                  onSuccess="${AssessmentItemInfo.onSuccessCallbackForProcessItem}"/>

