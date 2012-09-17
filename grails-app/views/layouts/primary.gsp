<%--
  Created by IntelliJ IDEA.
  User: relango
  Date: 4/3/12
  Time: 6:02 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page import="com.rialms.util.Less2Css;grails.util.Environment" contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html xmlns:m="http://www.w3.org/1998/Math/MathML" lang="en" ng-app="RialmsAngularApp">
<head>
    <title><g:layoutTitle/></title>

    <div id='head'>
        <link ng-repeat="stylesheet in itemStylesheets" ng-href="${request.contextPath}/{{stylesheet.href}}"
              type="{{stylesheet.type}}" rel="stylesheet" media="{{stylesheet.media}}" title="{{stylesheet.title}}"/>
    </div>
    <link href='http://fonts.googleapis.com/css?family=Hammersmith+One' rel='stylesheet' type='text/css'>
    <link href='http://fonts.googleapis.com/css?family=Droid+Serif:400italic' rel='stylesheet' type='text/css'>
    <link href='http://fonts.googleapis.com/css?family=Lato:400,300italic' rel='stylesheet' type='text/css'>
    <link href='http://fonts.googleapis.com/css?family=Droid+Sans:700' rel='stylesheet' type='text/css'>
    <script src="http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML"
            type="text/javascript"></script>
    <r:require module="core"/>
    <r:layoutResources/>
</head>

<body>
<r:script>
    $(document).ready(function () {
        initBootstrap();
    });
</r:script>
<qti:less2Css/>
<nav class="navbar navbar-fixed-top">
    <!-- TODO P3 Enable for dev -->
    <div class="navbar-inner">

        <div class="container">
            <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </a>
            <a class="brand" style="font-family: 'Droid Sans', sans-serif;color:#333;" href="${request.contextPath}">
                RIALMS
                <p style="font-family: 'Lato', sans-serif " ><g:message code="tagline.message"/></p>
            </a>

            <div class="nav-collapse">
                <ul class="nav">
                    <!--
                       <li  <%='item' == params.controller ? ' class="active"' : ''%>><a
                                href="${createLink(controller: 'item')}"><g:message code="item.label"/></a>
                        </li>
                        <li <%='test' == params.controller ? ' class="active"' : ''%>><a
                                href="${createLink(controller: 'test')}"><g:message code="tests.label"/></a></li> -->
                    <li <%='home' == params.controller ? ' class="active"' : ''%> >
                        <g:link controller="home"><g:message
                                code="home.label"/></g:link>
                    </li>
                    <li class="dropdown">
                        <a class="dropdown-toggle" data-toggle="dropdown">
                            <g:message code="demo.label"/>
                            <b class="caret"></b>
                        </a>
                        <ul class="dropdown-menu">
                            <li>
                                <g:link controller="demo" action="learnBasicAlgegra"><g:message
                                        code="LearnBasicAlgebra.label"/></g:link>
                            </li>
                            <li>
                                <g:link controller="demo" action="item"><g:message code="item.label"/></g:link>
                            </li>
                            <li>
                                <g:link controller="demo" action="test"><g:message code="test.label"/></g:link>
                            </li>
                        </ul>
                    </li>
                    <li <%='about' == params.controller ? ' class="active"' : ''%> >
                        <g:link controller="about"><g:message
                                code="about.label"/></g:link>
                    </li>
                    <li <%='contact' == params.controller ? ' class="active"' : ''%> >
                        <g:link controller="contact"><g:message
                                code="contact.label"/></g:link>
                    </li>

                </ul>
            </div>
        </div>
    </div>

</nav>
<div class="container-fluid">
    <g:layoutBody/>

    <hr>

    <footer>

    </footer>
</div>

<r:layoutResources/>
</body>
</html>