<%@ page import="com.rialms.assessment.test.SectionPartStatus; com.rialms.consts.Constants as Consts; com.rialms.consts.AssessmentItemStatus; com.rialms.assessment.item.AssessmentItemInfo" %>
<%--
  Created by IntelliJ IDEA.
  User: relango
  Date: 5/01/12
  Time: 12:37 AM
  To change this template use File | Settings | File Templates.
--%>


<div id="${Consts.testStatusContent}">
    <div class="span3" id="sidebar">
        <div class="sidebar-nav">
            <div class="block-header">
                <h4>
                    <g:message code="test.label"/>&nbsp;
                    <g:if test="${assessmentParams[Consts.testPart]}">
                        ${assessmentParams[Consts.testPart]}&nbsp;
                    </g:if>
                    <g:message code="test.status.message"/>
                </h4>
            </div>
            <!-- TODO make sure testpart is shown -->
            <ul class="nav nav-list">
                <g:each var='entry' in="${assessmentParams[Consts.testPartStatus]}">

                    <li class="nav-header">
                        <g:each var="parent" in="${entry.key.split(SectionPartStatus.PARENT_SECTION_DELIMITER)}">
                            ${parent}&nbsp;&rsaquo;
                        </g:each>
                    </li>

                    <g:each var='section' in="${entry.value}">
                        <li class="${section.isCurrentItem() ? 'active' : ''}">
                            <g:remoteLink action="navigate"
                                          onSuccess="${AssessmentItemInfo.onSuccessCallbackForProcessItem}"
                                          params="${params + [(Consts.renderItem): section.identifier, isPositionedAfterCurrent: section.isPositionedAfterCurrent()]}">
                                ${section.identifier} &nbsp;&nbsp; <span
                                    class="${section.status.statusClass}">${AssessmentItemStatus.format(section.status)}</span>
                            </g:remoteLink>
                        </li>
                    </g:each>

                </g:each>
            </ul>
        </div>
    </div>
</div>

