<%--
  Created by IntelliJ IDEA.
  User: relango
  Date: 3/12/12
  Time: 5:19 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page import="com.rialms.util.XmlUtils" contentType="text/html;charset=UTF-8" %>
<p>${XmlUtils.getNodeText(node)}</p>
   <g:each var="n" in="${node.childNodes()}" >

    <g:if test="${n.name().equalsIgnoreCase("p")}" >
        <g:render template="/renderer/renderPTag" model="[node:n]" />
    </g:if>

</g:each>