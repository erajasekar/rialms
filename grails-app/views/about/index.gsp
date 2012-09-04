<%--
  Created by IntelliJ IDEA.
  User: relango
  Date: 8/31/12
  Time: 11:07 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
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
            So we believe providing a interactive and adaptive self learning content is the best way to make anyone expert in their subject.

        </p>
        <blockquote>
            <p class="highlight">
            Our intent is to build a platform for delivering best self learning experience.
            </p>
        </blockquote>
    </div>

    <div>
        <h2>
            Why QTI?
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

    <div>
        <h2>
            QTI Compatibility
        </h2>

        <p>Currently, Rialms doesn't support all the features supported by <g:link
                url="http://www.imsglobal.org/question/qtiv2p1pd2/imsqti_implv2p1pd2.html"
                target="_blank">QTI spec</g:link>.
        The following table provides overview of what features are available and not support by rialms. TODO
        </p>
    </div>

    <div>
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