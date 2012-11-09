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
        <h2>
            What is <g:message code="project.name"/> ?
        </h2>

        <p>
            <g:message code="project.name"/> is a <span class="highlight">R</span>adical <span class="highlight">I</span>nteractive <span
                class="highlight">A</span>daptive <span class="highlight">L</span>earning <span
                class="highlight">M</span>anagement <span class="highlight">S</span>ystem.
            <g:message code="project.name"/> is a platform for delivering interactive and <g:link
                url="http://en.wikipedia.org/wiki/Adaptive_learning"
                target="_blank">adaptive learning</g:link> content.
            <blockquote><p><b>Adaptive learning</b> is an educational method which uses computers as interactive teaching devices.</p></blockquote>
            <p><g:message code="project.name"/> adapts the presentation of learning material according to students' learning needs, as indicated by their responses to questions.</p>
        </p>
    </div>

    <div class="span6">
        <h2>
            How it Works ?
        </h2>

        <p>
            <g:message code="project.name"/> is based on <g:link
                    url="http://en.wikipedia.org/wiki/QTI"
                    target="_blank">Question and Test Interoperability specification (QTI)</g:link>
            produced by <g:link url="http://www.imsglobal.org/" target="_blank">IMS Global</g:link>. <br/>
            QTI specification is a widely used standard for marking up questions and other learning material.
            <g:message code="project.name"/> is a delivery engine that can play QTI compatible content. All learning material in this site
            (Questions and Tests) are created as per QTI Spec. So <g:message code="project.name"/> can deliver more learning material by simply adding more content based on QTI Spec.

        <p>Check out these demos <i>(You can also use demo navigation menu)</i></p>
        <ul class="unstyled">
            <li><i class="icon-leaf"></i>
                <g:link controller="demo" action="learnBasicAlgegra"><g:message
                        code="LearnBasicAlgebra.label"/></g:link> show cases teaching basic algebra (Sixth grade) in interactive and adaptive manner.
            </li>
            <li><i class="icon-leaf"></i>
                <g:link controller="demo" action="item"><g:message
                        code="item.label"/></g:link> show cases all question features.
            </li>
            <li><i class="icon-leaf"></i>
                <g:link controller="demo" action="test"><g:message
                        code="test.label"/></g:link> show cases all test features.
            </li>
        </ul>
    </div>
</p>

</div>
</body>
</html>