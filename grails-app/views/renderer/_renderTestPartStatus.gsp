<%@ page import="com.rialms.consts.Constants as Consts; com.rialms.consts.AssessmentItemStatus; com.rialms.assessment.item.AssessmentItemInfo" %>
<%--
  Created by IntelliJ IDEA.
  User: relango
  Date: 5/01/12
  Time: 12:37 AM
  To change this template use File | Settings | File Templates.
--%>
<div id="${Consts.testNavigationStatus}">
    <h4>
        <g:message code="test.status.message"/>
    </h4>
<!-- TODO make sure testpart is shown -->
    <g:each var='entry' in="${assessmentParams[Consts.testPartStatus]}">

        <li>${entry.key}</li>
        <ul>
            <g:each var='section' in="${entry.value}">
                <li>
                    <g:if test="${section.isEnabled()}">
                        <g:remoteLink action="navigate"
                                      onSuccess="${AssessmentItemInfo.onSuccessCallbackForProcessItem}"
                                      params="${params + [(Consts.renderItem): section.identifier, isPositionedAfterCurrent: section.isPositionedAfterCurrent()]}">${section.identifier}</g:remoteLink>

                    </g:if>
                    <g:else>
                        ${section.identifier}
                    </g:else>
                    &nbsp;|&nbsp;
                    ${AssessmentItemStatus.format(section.status)}
                </li>
            </g:each>
        </ul>
    </g:each>
</div>


