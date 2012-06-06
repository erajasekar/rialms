<%@ page import="com.rialms.assessment.test.SectionPartStatus; com.rialms.consts.Constants as Consts; com.rialms.consts.AssessmentItemStatus; com.rialms.assessment.item.AssessmentItemInfo" %>
<%--
  Created by IntelliJ IDEA.
  User: relango
  Date: 5/01/12
  Time: 12:37 AM
  To change this template use File | Settings | File Templates.
--%>


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

            <ul class="nav nav-list" ng-repeat="(section,items) in testStatusModel">
                <li>{{hello}} </li>
            </ul>
        </div>
    </div>
</div>

