<%--
  Created by IntelliJ IDEA.
  User: Rajasekar Elango
  Date: 3/19/12
  Time: 10:54 PM
  To change this template use File | Settings | File Templates.
--%>

<g:if test="${(node.'@showHide'.equals("show")) && identifierValue?.equals(node.'@identifier')}">
    <g:render template="/renderer/renderItemBody" model="[node: node]"/>
</g:if>
<g:elseif
        test="${(node.'@showHide'.equals("hide")) && identifierValue && !(identifierValue.equals(node.'@identifier'))}">
    <g:render template="/renderer/renderItemBody" model="[node: node]"/>
</g:elseif>