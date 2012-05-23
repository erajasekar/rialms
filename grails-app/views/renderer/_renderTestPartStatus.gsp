<%@ page import="com.rialms.consts.Constants as Consts; com.rialms.consts.AssessmentItemStatus; com.rialms.assessment.item.AssessmentItemInfo" %>
<%--
  Created by IntelliJ IDEA.
  User: relango
  Date: 5/01/12
  Time: 12:37 AM
  To change this template use File | Settings | File Templates.
--%>

<div id="${Consts.testStatusContent}">

    <div class="span3 well sidebar-nav">
        <h4>
            <g:message code="test.status.message"/>
        </h4>
    <!-- TODO make sure testpart is shown -->
    <ul class="nav nav-list">
        <g:each var='entry' in="${assessmentParams[Consts.testPartStatus]}">

            <li class="nav-header">${entry.key}</li>

                <g:each var='section' in="${entry.value}">
                        <g:if test="${section.isEnabled()}">
                            <li>
                            <g:remoteLink action="navigate"
                                          onSuccess="${AssessmentItemInfo.onSuccessCallbackForProcessItem}"
                                          params="${params + [(Consts.renderItem): section.identifier, isPositionedAfterCurrent: section.isPositionedAfterCurrent()]}">
                                ${section.identifier} |  ${AssessmentItemStatus.format(section.status)}
                            </g:remoteLink>
                            </li>

                        </g:if>
                        <g:else>
                            <li class="active">
                                ${section.identifier} |  ${AssessmentItemStatus.format(section.status)}
                            </li>
                        </g:else>


                </g:each>

        </g:each>
    </ul>
    </div>
</div>

