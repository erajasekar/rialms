<%@ page import="com.rialms.consts.AssessmentItemStatus; com.rialms.assessment.item.AssessmentItemInfo" %>
<%--
  Created by IntelliJ IDEA.
  User: relango
  Date: 5/01/12
  Time: 12:37 AM
  To change this template use File | Settings | File Templates.
--%>

<h4>
    <g:message code="test.status.message"/>
</h4>
<% Stack<String> stack = new Stack<String>(); %>
<g:each var='entry' in="${assessmentParams.testPartStatus}">

    <li>${entry.key}</li>
    <ul>
        <g:each var='section' in="${entry.value}">
            <li>
                <g:if test="${section.isCurrentItem()}" >
                    *
                </g:if>
                <g:else>
                    &nbsp;
                </g:else>
                <g:remoteLink action="navigate" onSuccess="${AssessmentItemInfo.onSuccessCallbackForProcessItem}"
                              params="${params + [renderItem: section.identifier, isPositionedAfterCurrent: section.isPositionedAfterCurrent()]}">${section.identifier}</g:remoteLink>
                &nbsp;|&nbsp;
                ${AssessmentItemStatus.format(section.status)}
            </li>
        </g:each>
    </ul>
</g:each>


