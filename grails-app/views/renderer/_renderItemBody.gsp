<%--
  Created by IntelliJ IDEA.
  User: kavi
  Date: 3/13/12
  Time: 10:47 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
 <%

     System.out.println "${node.getClass()} == ${node.attributes()}";
 %>

<g:if test="${node.name().equalsIgnoreCase("p") && node instanceof groovy.util.slurpersupport.NodeChild}" >
    <g:set var="children" value="${node.getAt(0).children}" />
</g:if>
<g:else>
    <g:set var="children" value="${node.children()}" />
</g:else>

<g:each var="n" in="${children}" >

    <g:if test="${n instanceof String}" >
           ${n}
    </g:if>

    <g:elseif test="${n.name().equalsIgnoreCase("p")}">
        <p>
            <g:render template="/renderer/renderItemBody" model="[node:n]" />
        </p>
    </g:elseif>

    <g:elseif test="${n.name().equalsIgnoreCase("img")}">
         <g:set var="imgfile" value="${n.'@src'}"    />  <!--TODO -->
        <g:img dir="${exercisePath}/images"  file="sign.png" />
    </g:elseif>

    <g:elseif test="${n.name().equalsIgnoreCase("textEntryInteraction")}">
        <% System.out.println (n.attributes()) %>
        <g:set var="attribs" value="${n.attributes()}"  />
        <g:textField name="${attribs?.responseIdentifier}" />
    </g:elseif>

    <g:elseif test="${n.name().equalsIgnoreCase("feedbackBlock") && (n.'@showHide'.equals("show")) && outcome?.PROGRESS.toString().equals("step2")}">
            <g:render template="/renderer/renderItemBody" model="[node:n]" />
    </g:elseif>
</g:each>
