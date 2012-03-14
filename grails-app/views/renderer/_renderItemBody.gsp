<%--
  Created by IntelliJ IDEA.
  User: kavi
  Date: 3/13/12
  Time: 10:47 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>

<g:each var="n" in="${node.children()}" >
    <g:if test="${n.name().equalsIgnoreCase("p")}">
        <p>  ${n.text()}

        <g:render template="/renderer/renderItemBody" model="[node:n]" />

        </p>

    </g:if>

    <g:if test="${n.name().equalsIgnoreCase("img")}">
         <g:set var="imgfile" value="${n.'@src'}"    />  <!--TODO -->
        <g:img dir="${exercisePath}/images"  file="sign.png" />
    </g:if>

    <g:if test="${n.name().equalsIgnoreCase("textEntryInteraction")}">
        <g:set var="id" value="${n.'@responseIdentifier'}"  />
        <g:textField name="${id}" />
    </g:if>

    <g:if test="${n.name().equalsIgnoreCase("feedbackBlock") && (n.'@showHide'.equals("show")) && outcome?.PROGRESS.toString().equals("step2")}">

        <g:render template="/renderer/renderItemBody" model="[node:n]" />

    </g:if>
</g:each>
