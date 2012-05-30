<%@ page import="com.rialms.assessment.item.AssessmentItemInfo; com.rialms.consts.Constants as Consts" %>
<%--
  Created by IntelliJ IDEA.
  User: Rajasekar Elango
  Date: 4/19/12
  Time: 11:55 PM
  To change this template use File | Settings | File Templates.
--%>
<div id='testContent'>
    <g:render template="/renderer/renderAssessmentTitle" model="[assessmentTitle: assessmentItemInfo.title]"/>
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
                    class="btn btn-success">
                <i class="icon-ok icon-white"></i>Submit

            </button>

        </g:if>
        <g:else>
            <button type='submit'
                    value='Submit'
                    id='submit'
                    class="btn btn-success"
                    disabled="disabled">
                <i class="icon-ok icon-white"></i>Submit
            </button>
        </g:else>
    </div>
</div>