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

    <g:render template="/renderer/renderItemBody" model="[node:xmlRoot.itemBody[0], outcome:outcome, dataPath:dataPath]" />

 <g:submitButton value="Enter" name="processButton"/>
 </g:form>

<g:if test="${outcome}">
---------------------------------
     <br/>
    ${outcome}
</g:if>
