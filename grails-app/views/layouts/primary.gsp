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
                <a class="brand" href="${request.contextPath}">Rialms</a>

                <div class="nav-collapse">
                    <ul class="nav">
                        <li  <%='demo' == params.controller ? ' class="active"' : ''%>><a
                                href="${createLink(controller: 'demo')}"><g:message code="demo.label"/></a>
                        </li>
                      <!--  <li  <%='item' == params.controller ? ' class="active"' : ''%>><a
                                href="${createLink(controller: 'item')}"><g:message code="item.label"/></a>
                        </li>
                        <li <%='test' == params.controller ? ' class="active"' : ''%>><a
                                href="${createLink(controller: 'test')}"><g:message code="tests.label"/></a></li> -->
                    </ul>
                </div>
            </div>
        </div>

</nav>

<div class="container-fluid">
    <g:layoutBody/>

    <hr>

    <footer>
        <p>&copy; Company 2011</p>
    </footer>
</div>

<r:layoutResources/>
</body>
</html>