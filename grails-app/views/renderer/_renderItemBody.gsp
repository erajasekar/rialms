<%--
  Created by IntelliJ IDEA.
  User: kavi
  Date: 3/13/12
  Time: 10:47 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page import="com.rialms.consts.Tag" contentType="text/html;charset=UTF-8" %>

<g:each var="n" in="${node.children()}">
    <g:if test="${!(n instanceof String)}">
       <% System.out.println "isMixed " + n.name() + " ==> " + Tag.isMixedTag(n.name());     %>
    </g:if>
    <g:if test="${n instanceof String}">
        ${n}
    </g:if>

    <g:elseif test="${Tag.isMixedTag(n.name())}">
        <${n.name().getLocalPart()}>
            <g:render template="/renderer/renderItemBody" model="[node:n]"/>
        </${n.name().getLocalPart()}>
    </g:elseif>

    <g:elseif test="${Tag.img.equals(n.name())}">
        <qti:img dir="${exercisePath}" file="${n.'@src'}" alt="${n.'@alt'}"/>
    </g:elseif>

    <g:elseif test="${Tag.textEntryInteraction.equals(n.name())}">
        <qti:textEntryInteraction xmlAttributes="${n.attributes()}" responseValues="${responseValues}" />
    </g:elseif>

    <g:elseif test="${Tag.choiceInteraction.equals(n.name())}">
            <qti:choiceInteraction xmlNode="${n}" responseValues="${responseValues}" outcome="${outcome}" exercisePath="${exercisePath}"/>
    </g:elseif>

    <g:elseif test="${Tag.feedbackBlock.equals(n.name())}">
        <g:if test="${(n.'@showHide'.equals("show")) && outcome?.PROGRESS.toString().equals(n.'@identifier')}">
            <g:render template="/renderer/renderItemBody" model="[node:n]"/>
        </g:if>
    </g:elseif>
</g:each>
