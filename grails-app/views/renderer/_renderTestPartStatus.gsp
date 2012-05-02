<%@ page import="com.rialms.consts.AssessmentItemStatus; com.rialms.assessment.item.AssessmentItemInfo" %>
<%--
  Created by IntelliJ IDEA.
  User: relango
  Date: 5/01/12
  Time: 12:37 AM
  To change this template use File | Settings | File Templates.
--%>

<h4>
    <g:message code="test.status.message"/>
</h4>
<% Stack<String> stack = new Stack<String>(); %>
<ul>
    <g:each var='section' in="${assessmentParams.testPartStatus}">
        <g:if test="${section.isItemRef()}">
            <g:if test="${!stack.isEmpty()}" >
                <% String top = stack.peek();
                   if (top != 'li'){
                       stack.push('li');
                   }
                %>
            </g:if>
            <li>
                <g:remoteLink action="navigate" onSuccess="${AssessmentItemInfo.onSuccessCallbackForProcessItem}"
                              params="${params + [renderItem: section.identifier, isPositionedAfterCurrent: section.isPositionedAfterCurrent()]}">${section.identifier}</g:remoteLink>
                &nbsp;|&nbsp;
                ${AssessmentItemStatus.format(section.status)}
            </li>
        </g:if>
        <g:else>
            <g:if test="${!stack.isEmpty()}" >
                <% top = stack.peek(); %>
                <g:if test="${top == 'li'}">
                    </ul>
                    <% stack.pop() %>
                </g:if>
            </g:if>
            <li>${section.identifier}</li>
            <ul>
            <% stack.push('ul') %>
        </g:else>
    </g:each>
    <g:each in="${stack.findAll {it == 'ul'}}">
        </ul>
    </g:each>
</ul>


