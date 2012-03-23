<%--
  Created by IntelliJ IDEA.
  User: Rajasekar Elango
  Date: 3/19/12
  Time: 10:54 PM
  To change this template use File | Settings | File Templates.
--%>
<g:if test="${(node.'@showHide'.equals("show")) && identifierValue?.split(',')?.contains(node.'@identifier')}">
    <g:render template="/renderer/renderItemSubTree" model="[node: node, assessmentItemInfo:assessmentItemInfo]"/>
</g:if>
<g:elseif
        test="${(node.'@showHide'.equals("hide")) && identifierValue && !(identifierValue?.split(',')?.contains(node.'@identifier'))}">
    <g:render template="/renderer/renderItemSubTree" model="[node: node, assessmentItemInfo:assessmentItemInfo]"/>
</g:elseif>