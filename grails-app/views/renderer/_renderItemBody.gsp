<%--
  Created by IntelliJ IDEA.
  User: kavi
  Date: 3/13/12
  Time: 10:47 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page import="com.rialms.consts.Tag" contentType="text/html;charset=UTF-8" %>

<g:each var="n" in="${node.children()}">

    <g:if test="${n instanceof String}">
        ${n}
    </g:if>
    <g:else>
        <g:set var="tag" value="${Tag.valueOf(n.name())}" scope="page"/>
        <% System.out.println "mixed tag ${tag} ${n.name()}" %>
        <g:if test="${Tag.isMixedTag(tag)}">
            <${n.name().getLocalPart()}>
            <g:render template="/renderer/renderItemBody" model="[node: n]"/>
            </${n.name().getLocalPart()}>
        </g:if>

        <g:elseif test="${tag == Tag.img}">
            <qti:img dir="${exercisePath}" file="${n.'@src'}" alt="${n.'@alt'}"/>
        </g:elseif>

        <g:elseif test="${tag == Tag.textEntryInteraction}">
            <% System.out.println "found ${n.'@responseIdentifier'}" %>
            <qti:textEntryInteraction xmlAttributes="${n.attributes()}" responseValues="${responseValues}"/>
        </g:elseif>

        <g:elseif test="${tag == Tag.choiceInteraction}">
            <qti:choiceInteraction xmlNode="${n}" responseValues="${responseValues}" outcome="${outcome}"
                                   exercisePath="${exercisePath}"/>
        </g:elseif>

        <g:elseif test="${tag == Tag.inlineChoiceInteraction}">
            <qti:inlineChoiceInteraction xmlNode="${n}" responseValues="${responseValues}" outcome="${outcome}" />
        </g:elseif>

        <g:elseif test="${tag == Tag.feedbackBlock}">
            <g:if test="${(n.'@showHide'.equals("show")) && outcome?.PROGRESS.toString().equals(n.'@identifier')}">
                <% System.out.println "here" %>
                <g:render template="/renderer/renderItemBody" model="[node: n]"/>
            </g:if>
        </g:elseif>

    </g:else>

</g:each>
