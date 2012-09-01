<%--
  Created by IntelliJ IDEA.
  User: relango
  Date: 8/31/12
  Time: 11:03 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="primary"/>
    <title><g:message code="project.name"/> - <g:message code="home.label"/></title>
</head>

<body>
<div class="documentation row-fluid">
    <div class="span6">
        <h2 >
            What is Rialms ?
        </h2>

        <p>
            Rialms is <span class="highlight">R</span>adical <span class="highlight">I</span>nteractive <span
                class="highlight">A</span>daptive <span class="highlight">L</span>earning <span
                class="highlight">M</span>anagement <span class="highlight">S</span>ystem.
        It is a platform for delivering interactive and adaptive learning content based on <g:link
                url="http://en.wikipedia.org/wiki/QTI"
                target="_blank">Question and Test Interoperability specification (QTI)</g:link>
        produced by <g:link url="http://www.imsglobal.org/" target="_blank">IMS Global</g:link>
        </p>
    </div>

    <div class="span6">
        <h2>
            How it Works ?
        </h2>

        <p>
            <g:link url="http://en.wikipedia.org/wiki/QTI"
                    target="_blank">QTI</g:link> specification describes a data model for representation of question and test content in a XML format.
            Rialms is a delivery engine that can play <g:link url="http://en.wikipedia.org/wiki/QTI"
                                                                target="_blank">QTI</g:link> compatible content.

        <h3>Check out demos below</h3>
        <ul class="unstyled">
            <li >
                <g:link controller="demo" action="learnBasicAlgegra"><g:message
                        code="LearnBasicAlgebra.label"/></g:link> show cases teaching basic algebra (Sixth grade) in interactive and adaptive manner.
            </li>
            <li>
                <g:link controller="demo" action="item"><g:message
                        code="item.label"/></g:link> show cases all question features.
            </li>
            <li>
                <g:link controller="demo" action="test"><g:message
                        code="test.label"/></g:link> show cases all test features.
            </li>
        </ul>
    </div>
</p>

</div>
</body>
</html>