<%--
  Created by IntelliJ IDEA.
  User: Rajasekar Elango
  Date: 3/11/12
  Time: 10:43 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<head>
 <title> ${xmlRoot.'@title'} </title>
</head>
<g:each var="n" in="${xmlRoot.itemBody.childNodes()}" >

    <g:if test="${n.name().equalsIgnoreCase("p")}" >
        <g:render template="/renderer/renderPTag" model="[node:n]" />
    </g:if>

</g:each>


