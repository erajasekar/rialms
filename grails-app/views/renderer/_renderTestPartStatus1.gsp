<%@ page import="com.rialms.angular.JsObjectUtil; grails.converters.JSON; com.rialms.assessment.test.SectionPartStatus; com.rialms.consts.Constants as Consts; com.rialms.consts.AssessmentItemStatus; com.rialms.assessment.item.AssessmentItemInfo" %>
<%--
  Created by IntelliJ IDEA.
  User: relango
  Date: 5/01/12
  Time: 12:37 AM
  To change this template use File | Settings | File Templates.
--%>
<r:script>
$(document).ready(function(){
    initTestStatusModel(${assessmentParams[Consts.testStatusModel] as JSON});
  });

</r:script>

<div id="${Consts.testStatusContent}" ng-controller="TestStatusController">
    <div class="span3" id="sidebar">
        <div class="sidebar-nav">
            <div class="block-header">
                <h4>
                    <g:message code="test.label"/>
                    <g:if test="${assessmentParams[Consts.testPart]}">
                        ${assessmentParams[Consts.testPart]}
                    </g:if>
                    <g:message code="test.status.message"/>
                </h4>
            </div>
            <% JsObjectUtil.PropertyConstructor statusEntry = new JsObjectUtil.PropertyConstructor(Consts.statusEntry) %>
            <ul class="nav nav-list" ng-repeat="statusEntry in getStatusEntries()">
                <li ng-class="getStyleClass(statusEntry)">
                    <span ng-show="${statusEntry.getProperty(Consts.isSectionTitle)}">
                        ${statusEntry.getPropertyValue(Consts.identifier)}
                    </span>
                    <g:remoteLink action="navigate" ng-hide="${statusEntry.getProperty(Consts.isSectionTitle)}"
                                  onSuccess="${AssessmentItemInfo.onSuccessCallbackForProcessItem}"
                                  params="${params + [(Consts.renderItem): statusEntry.getPropertyValue(Consts.identifier), isPositionedAfterCurrent: statusEntry.getPropertyValue(Consts.isPositionedAfterCurrent)]}">
                        ${statusEntry.getPropertyValue(Consts.identifier)} &nbsp;&nbsp;
                        <span class="${statusEntry.getPropertyValue(Consts.styleClass)}">${statusEntry.getPropertyValue(Consts.status)}</span>
                    </g:remoteLink>
                </li>

            </ul>

            <div class="form-actions">
                <div class="btn-group">
                    <button class="btn btn-info dropdown-toggle" data-toggle="dropdown" href="#">
                        <g:message code="show.label"/>&nbsp;&nbsp;{{filterStatus}}
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu">
                        <g:each var="status" in="${AssessmentItemStatus.allStatuses()}">
                            <li ng-show="filterStatus != '${status}'"><a href="#" ng-click="filterStatus='${status}'" >${status}</a></li>
                        </g:each>
                    </ul>
                </div>
            </div>

        </div>

    </div>
</div>

