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
            <ul class="nav nav-list" ng-repeat="statusEntry in getStatusEntries(testStatusModel)">
                <div ng-init="cls=getStyleClass(statusEntry)">{{cls}}</div>
                <li ng-class="getStyleClass(statusEntry)">${JsObjectUtil.getTemplateVar('statusEntry', Consts.identifier)}</li>
            </ul>
        </div>
    </div>
</div>

