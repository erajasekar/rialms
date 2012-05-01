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

<g:each var='section' in="${assessmentParams.testPartStatus}">
    <g:if test="${section.isItemRef()}">

        <g:remoteLink action="navigate" onSuccess="${AssessmentItemInfo.onSuccessCallbackForProcessItem}"
                      params="${params + [renderItem: section.identifier, isPositionedAfterCurrent: section.isPositionedAfterCurrent()]}">${section.identifier}</g:remoteLink>
        &nbsp;|&nbsp;
        ${AssessmentItemStatus.format(section.status)}
    </g:if>
    <g:else>
        ${section.identifier}
    </g:else>

    <br/>
</g:each>