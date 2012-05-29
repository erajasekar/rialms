<%@ page import="com.rialms.assessment.item.AssessmentItemInfo; com.rialms.consts.Constants as Consts" %>
<%--
  Created by IntelliJ IDEA.
  User: Rajasekar Elango
  Date: 4/19/12
  Time: 11:55 PM
  To change this template use File | Settings | File Templates.
--%>
<div id='testContent'>
    <div class="block-header">
        <h4>${assessmentItemInfo.title} <span class="pull-right">
            <g:link action="report" params="[id: params.id]" rel="tooltip"
                    title="${message(code: 'button.report.label')}">
                <i class="icon-signal"></i></g:link>

            <g:link name='exit' action="reset" params="[id: params.id, redirectto: 'list']"
                    onclick="return confirm(\'${g.message(code: 'test.exit.confirm.message')}\')"
                    title="${message(code: 'button.exitTest.label')}">
                <i class="icon-remove"></i>
            </g:link></span></h4>
    </div>
    <div class="block-content">

        <g:if test="${assessmentParams[Consts.timeRemaining] > 0}">
            <script type="text/javascript">
                initTimer("${assessmentParams[Consts.timeRemaining]}")
            </script>
        </g:if>

        <g:if test="${assessmentParams.rubric}">

            <g:each var="section" in="${assessmentParams.rubric.children()}">
                <g:render template="/renderer/renderItemSubTree" model="[node: section.children().get(0)]"/>
            </g:each>

        </g:if>
        <g:render template="/renderer/renderItemSubTree"
                  model="[node: assessmentItemInfo.xmlRoot, assessmentItemInfo: assessmentItemInfo]"/>

        <g:hiddenField name="id" value="${params.id}"/>
        <g:hiddenField name="questionId" value="${assessmentParams[Consts.questionId]}"/>
        <g:hiddenField name="submitClicked"
                       value="true"/> <!-- Used to indicate if submit button was clicked. This param will be set only if submit was clicked -->
        <g:if test="${assessmentParams.submitEnabled}">
            <button type='submit'
                    value='Submit'
                    id='submit'
                    class="btn btn-primary">
                <i class="icon-ok icon-white"></i>Submit

            </button>

        </g:if>
        <g:else>
            <button type='submit'
                    value='Submit'
                    id='submit'
                    class="btn btn-primary"
                    disabled="disabled">
                <i class="icon-ok icon-white"></i>Submit
            </button>
        </g:else>
    </div>
</div>