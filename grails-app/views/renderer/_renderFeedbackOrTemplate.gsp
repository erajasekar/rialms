<%@ page import="com.rialms.consts.VisibilityMode" %>
<%--
  Created by IntelliJ IDEA.
  User: Rajasekar Elango
  Date: 3/19/12
  Time: 10:54 PM
  To change this template use File | Settings | File Templates.
--%>
<g:if test="${(node.'@showHide'.equals(VisibilityMode.SHOW_IF_MATCH.toString())) && identifierValue?.split(',')?.contains(node.'@identifier')}">
    <g:render template="/renderer/renderItemSubTree" model="[node: node, assessmentItemInfo: assessmentItemInfo]"/>
</g:if>
<g:elseif
        test="${(node.'@showHide'.equals(VisibilityMode.HIDE_IF_MATCH.toString())) && identifierValue && !(identifierValue?.split(',')?.contains(node.'@identifier'))}">
    <g:render template="/renderer/renderItemSubTree" model="[node: node, assessmentItemInfo: assessmentItemInfo]"/>
</g:elseif>