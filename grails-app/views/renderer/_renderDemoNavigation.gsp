<%--
  Created by IntelliJ IDEA.
  User: Rajasekar Elango
  Date: 08/26/12
  Time: 10:48 PM
  To change this template use File | Settings | File Templates.
--%>
<div class="row-fluid">
   <div class="span3">&nbsp;</div>
    <div class="span6 subnav">
        <ul class="nav nav-pills">
            <li <%='play' == params.action && 'test' == params.controller ? ' class="active"' : ''%>>
                <g:link controller="demo" action="learnBasicAlgegra"><g:message code="LearnBasicAlgebra.label"/></g:link>
            </li>
            <li <%='item' == params.action ? ' class="active"' : ''%>>
                <g:link controller="demo" action="item"><g:message code="item.label"/></g:link>
            </li>
            <li <%='test' == params.action ? ' class="active"' : ''%>>
                <g:link controller="demo" action="test"><g:message code="test.label"/></g:link>
            </li>
        </ul>
    </div>
</div>