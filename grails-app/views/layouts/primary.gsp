<%--
  Created by IntelliJ IDEA.
  User: relango
  Date: 4/3/12
  Time: 6:02 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html xmlns:m="http://www.w3.org/1998/Math/MathML" lang="en">
<head>
    <title><g:layoutTitle/></title>
    <r:require module="core"/>
    <r:layoutResources/>
</head>

<body>
<nav class="navbar navbar-fixed-top">
    <div class="navbar-inner">

        <div class="container-fluid">
            <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </a>
            <a class="brand" href="${request.contextPath}">Rialms</a>

            <div class="btn-group pull-right">

                <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
                    <i class="icon-user"></i> Username
                    <span class="caret"></span>
                </a>
                <ul class="dropdown-menu">
                    <li><a href="#">Profile</a></li>
                    <li class="divider"></li>
                    <li><a href="#">Sign Out</a></li>

                </ul>
            </div>

            <div class="nav-collapse">
                <ul class="nav">
                    <li class="active"><a href="${createLink(controller: 'item')}"><g:message code="item.label"/></a>
                    </li>
                    <li <%='test' == controllerName ? ' class="active"' : ''%>><a
                            href="${createLink(controller: 'test')}"><g:message code="test.label"/></a></li>
                </ul>
            </div><!--/.nav-collapse -->
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