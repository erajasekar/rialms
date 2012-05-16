<%@ page import="com.rialms.consts.AssessmentItemStatus; com.rialms.assessment.item.AssessmentItemInfo; com.rialms.consts.Constants as Consts" %>
<%--
  Created by IntelliJ IDEA.
  User: relango
  Date: 4/26/12
  Time: 12:37 AM
  To change this template use File | Settings | File Templates.
--%>
<h4>
    <g:if test="${assessmentParams[Consts.testPart]}">
        <g:message code="testpart.submission.confirm.message" args="${[assessmentParams[Consts.testPart]]}"/>
    </g:if>
    <g:else>
        <g:message code="test.submission.confirm.message"/>
    </g:else>
</h4>

<g:render template="/renderer/renderTestPartStatus" model="[assessmentParams: assessmentParams]"/>

<g:hiddenField name="id" value="${params.id}"/>

<button id="submit" type="button" class="btn btn-success"
        onclick="${remoteFunction(action: 'submitTestPart', onSuccess: AssessmentItemInfo.onSuccessCallbackForProcessItem, params: [id: params.id])}">
    <i class="icon-ok icon-white"></i>Submit
</button>
