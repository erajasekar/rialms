<%--
  Created by IntelliJ IDEA.
  User: relango
  Date: 3/28/12
  Time: 3:17 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page import="groovy.xml.XmlUtil; com.rialms.consts.Constants as Consts" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="primary"/>
    <title>${assessmentParams[Consts.title]}</title>
</head>

<body>
<r:script>
    $(document).ready(function () {
        initTestRendering();
    });
</r:script>
<g:render template="/renderer/renderTestTitle" model="[testTitle: assessmentParams[Consts.title]]"/>
<div class="row-fluid">
    <div class="span2">&nbsp;</div>

    <div class="span8">
        <g:if test="${flash.message}">
            <g:message code="${flash.message}"/>
        </g:if>
        <div class="alert alert-success">
            <a class='close' data-dismiss='alert' href='#'>&times;</a>
            <h4><g:message code="test.complete.message"/></h4>
        </div>
        <g:if test="${assessmentParams[Consts.assessmentFeedback] || assessmentParams[Consts.testPartFeedback]}">
            <g:render template="/renderer/renderAssessmentTitle"
                      model="[assessmentTitle: g.message(code: 'test.feedback.message')]"/>
            <div class="block-content">
                <g:render template="/renderer/renderTestFeedback"/>
            </div>
        </g:if>
        <g:render template="/renderer/renderReportAndExitButtons" />

    </div>
</div>
</body>
</html>