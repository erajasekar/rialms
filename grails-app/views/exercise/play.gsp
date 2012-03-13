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
<g:form name="ExerciseForm"  action="play" >
<g:each var="n" in="${xmlRoot.itemBody.depthFirst()}" >

   <g:if test="${n.name().equalsIgnoreCase("p")}" >
        <g:render template="/renderer/renderPTag" model="[node:n]" />
    </g:if>
   <g:if test="${n.name().equalsIgnoreCase("textEntryInteraction")}" >
        <g:render template="/renderer/textEntryInteractionTag" model="[node:n]" />
    </g:if>

</g:each>
 <g:submitButton value="Enter" name="processButton"/>
 </g:form>

<g:if test="${outcome}">
---------------------------------
     <br/>
    ${outcome}
</g:if>
