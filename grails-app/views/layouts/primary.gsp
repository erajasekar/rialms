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

        <div class="container-fluid">
            <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </a>
            <a class="brand logo" href="${request.contextPath}">
                <%= g.message(code:'project.name').toUpperCase() %>
                <span class="tagline"><g:message code="tagline.message"/></span>
            </a>

            <div class="nav-collapse">
                <ul class="nav">
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
    
    	<r:script>
    
		var _gaq = _gaq || [];
		_gaq.push(['_setAccount', 'UA-38723778-1']);
		_gaq.push(['_trackPageview']);

		(function() {
		var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
		ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
		var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
		})();
    
	</r:script>

    </footer>
</div>

<r:layoutResources/>
</body>
</html>