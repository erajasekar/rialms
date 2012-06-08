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
            <ul class="nav nav-list" ng-repeat="statusEntry in getStatusEntries(testStatusModel)">
                <li ng-class="getStyleClass(statusEntry)">
                    <span ng-show="${statusEntry.getProperty(Consts.isSectionTitle)}">
                        ${statusEntry.getPropertyValue(Consts.identifier)}
                    </span>
                        <g:remoteLink action="navigate" ng-hide="${statusEntry.getProperty(Consts.isSectionTitle)}"
                                      onSuccess="${AssessmentItemInfo.onSuccessCallbackForProcessItem}"
                                      params="${params + [(Consts.renderItem): statusEntry.getPropertyValue(Consts.identifier), isPositionedAfterCurrent:statusEntry.getPropertyValue(Consts.isPositionedAfterCurrent)]}"
                        >
                            ${statusEntry.getPropertyValue(Consts.identifier)} &nbsp;&nbsp;
                            <span class="${statusEntry.getPropertyValue(Consts.styleClass)}">${statusEntry.getPropertyValue(Consts.status)}</span>
                        </g:remoteLink>
                </li>
            </ul>
        </div>
        <div class="block-controls">
            <div class="btn-group">
                <a class="btn btn-info dropdown-toggle" data-toggle="dropdown" href="#">
                    <g:message code="show.label" />
                    <span class="caret"></span>
                </a>
                <ul class="dropdown-menu">
                   <li><a href="#">Presented</a></li>
                  <li><a href="#">Not Presented</a></li>
                </ul>
            </div>
            <div class="btn-group">
                <button class="btn dropdown-toggle" data-toggle="dropdown">Action <span class="caret"></span></button>
                <ul class="dropdown-menu">
                    <li><a href="#">Action</a></li>
                    <li><a href="#">Another action</a></li>
                    <li><a href="#">Something else here</a></li>
                    <li class="divider"></li>
                    <li><a href="#">Separated link</a></li>
                </ul>
            </div><!-- /btn-group -->
        </div>
    </div>
</div>

