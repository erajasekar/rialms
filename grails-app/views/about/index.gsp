<%--
  Created by IntelliJ IDEA.
  User: relango
  Date: 8/31/12
  Time: 11:07 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page import="com.rialms.qti.QtiCompatibility;" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="primary"/>
    <title><g:message code="project.name"/> - <g:message code="about.label"/></title>
</head>

<body>
<div class="documentation row-fluid">

<div>
    <h2>
        Our Vision
    </h2>

    <p>
        We believe in famous chinese proverb
    </p>
    <blockquote>
        <p>
            “Tell me and I'll forget; <br/>
            show me and I may remember; <br/>
            involve me and I'll understand.” <br/>
        </p>
    </blockquote>

    <p>
        So we believe providing a interactive and adaptive learning material is the best way to create involvement and better understanding of the subject.
    </p>
    <blockquote>
        <p class="highlight">
            Our intent is to build a platform for delivering best self learning experience.
        </p>
    </blockquote>
</div>

<div>
    <h2>
        About QTI
    </h2>

    <p>
        <g:link url="http://www.imsglobal.org/"
                target="_blank">IMS Global</g:link>  Learning Consortium is an industry and academic consortium that develops specifications for interoperable learning technology.
        It has produced a <g:link
                url="http://www.imsglobal.org/question/index.html"
                target="_blank">Question and Test Interoperability specification (QTI)</g:link>  that describes a data model for the representation of question and test data and their corresponding results reports.
        Therefore the question and test content can be easily imported to and exported from many other learning management systems that
        <g:link
                url="http://en.wikipedia.org/wiki/QTI#Applications_with_IMS_QTI_support"
                target="_blank">support QTI.</g:link>
    </p>

    <p>
        QTI XML not only describes the static parts of questions, and tests (text, layout, order, and so on) but also their dynamic behavior - for example, how to behave when the user gives a wrong answer, when to provide certain feedback, and how to calculate the score. So content can be easily customized to support many practical use cases.
    </p>
</div>

<div class="row-fluid">
    <h2>
        QTI Compatibility
    </h2>

    <p><g:link title="QTI Implementation Guide"
               url="http://www.imsglobal.org/question/qtiv2p1pd2/imsqti_implv2p1pd2.html"
               target="_blank">QTI spec</g:link> is generalized to support wide range of features. We want to iteratively deliver most useful features instead of implementing everything with some features never being used.
        <br/>For list of all features supported by QTI Spec, please refer to
    <g:link title="QTI Implementation Guide"
            url="http://www.imsglobal.org/question/qtiv2p1pd2/imsqti_implv2p1pd2.html"
            target="_blank">QTI Implementation Guide</g:link>

    The following list provides most common features supported by Rialms.

    <div class="span4">
        <ul class="unstyled">
            <p class="highlight">
                <g:message code='item.label'/>&nbsp<g:message code='features.label'/>
            </p>
            <g:each in="${QtiCompatibility.itemCompatibilityList}" var="i">
                <g:if test="${i.supported}">
                    <li>&nbsp;&nbsp; <i class="icon-thumbs-up"></i>
                        ${i.feature}
                    </li>
                </g:if>
            </g:each>
        </ul>
    </div>

    <div class="span5">
        <ul class="unstyled">
            <p class="highlight">
                <g:message code='test.label'/>&nbsp<g:message code='features.label'/>
            </p>
            <g:each in="${QtiCompatibility.testCompatibilityList}" var="i">
                <g:if test="${i.supported}">
                    <li>&nbsp;&nbsp; <i class="icon-thumbs-up"></i>
                        ${i.feature}
                    </li>
                </g:if>
            </g:each>
        </ul>
    </div>

    <div class="row-fluid">
        <div class="span12">
            <p class="highlight">Check out these demos to see them in action <i>(You can also use demo navigation menu)</i>
            </p>
            <ul class="unstyled">
                <li>&nbsp;&nbsp;<i class="icon-leaf"></i>
                    <g:link controller="demo" action="learnBasicAlgegra"><g:message
                            code="LearnBasicAlgebra.label"/></g:link> show cases teaching basic algebra (Sixth grade) in interactive and adaptive manner.
                </li>
                <li>&nbsp;&nbsp;<i class="icon-leaf"></i>
                    <g:link controller="demo" action="item"><g:message
                            code="item.label"/></g:link> show cases all question features.
                </li>
                <li>&nbsp;&nbsp;<i class="icon-leaf"></i>
                    <g:link controller="demo" action="test"><g:message
                            code="test.label"/></g:link> show cases all test features.
                </li>
            </ul>
        </div>
    </div>

</p>
</div>

<div class="row-fluid">
    <div class="span12">
        <h2>
            Technology
        </h2>

        <p>
            Riamls is build using these popular technologies and open source libraries.
        <ul class="unstyled">
            <li>
                <i class="icon-thumbs-up"></i>
                <g:link
                        url="http://qtitools.org/"
                        target="_blank">QTI Tools</g:link> library for processing
                <g:link
                        url="http://www.imsglobal.org/question/qtiv2p1pd2/imsqti_implv2p1pd2.html"
                        target="_blank">QTI v2.1</g:link> questions and tests.
            </li>
            <li>
                <i class="icon-thumbs-up"></i>
                <g:link
                        url="http://grails.org/"
                        target="_blank">Grails</g:link> web application framework.
            </li>
            <li>
                <i class="icon-thumbs-up"></i>
                <g:link
                        url="http://angularjs.org/"
                        target="_blank">AngularJS</g:link> javascript framework.
            </li>
            <li>
                <i class="icon-thumbs-up"></i>
                <g:link
                        url="http://jquery.com//"
                        target="_blank">jQuery</g:link> javascript library.
            </li>
            <li>
                <i class="icon-thumbs-up"></i>
                <g:link
                        url="http://twitter.github.com/bootstrap/"
                        target="_blank">Twitter Bootstrap</g:link> front-end toolkit.
            </li>
            <li>
                <i class="icon-thumbs-up"></i>
                <g:link
                        url="http://coffeescript.org/"
                        target="_blank">CoffeeScript</g:link> language that transcompiles to javascript.
            </li>
            <li>
                <i class="icon-thumbs-up"></i>
                <g:link
                        url="http://jsplumb.org/"
                        target="_blank">JsPlumb</g:link> library to connect elements on webpage.
            </li>

        </ul>
    </p>
    </div>
</div>

<div>
    <h2>
        Roadmap
    </h2>

    <p class="highlight">
        <b>Rialms 0.1</b>
    </p>

    <p>
        Current version Rialms 0.1 is a alpha version that showcases demo questions and tests.
    </p>

    <p class="highlight">
        <b>Rialms 1.0</b>
    </p>

    <p>
        Provide ability to upload QTI content and share it publicly with everyone.
    </p>

    <p class="highlight">
        <b>Rialms 1.1</b>
    </p>

    <p>
        Provide ability share uploaded QTI content with specific list of people or with everyone.
    </p>
</div>
</div>

</body>
</html>