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
        <g:render template="/renderer/renderAssessmentTitle"
                  model="[assessmentTitle: g.message(code: 'test.feedback.message')]"/>
        <div class="block-content">
            <g:render template="/renderer/renderTestFeedback"/>
            <br/>
            <div>
                <g:link action="report" params="[id: params.id]" class="btn btn-primary" target="_blank"><i
                        class="icon-signal icon-white"></i> Report</g:link>

                <g:link name='exit' action="reset" params="[id: params.id, redirectto: 'list']"
                        class="btn btn-danger"
                        onclick="return confirm(\'${g.message(code: 'test.exit.confirm.message')}\')"><i
                        class="icon-remove icon-white"></i> Exit Test</g:link>
            </div>
        </div>
    </div>
</div>
</body>
</html>