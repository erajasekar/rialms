<%--
  Created by IntelliJ IDEA.
  User: Rajasekar Elango
  Date: 04/02/12
  Time: 05:20 PM
  To change this template use File | Settings | File Templates.
--%>
<ul>
    <g:message code="item.validation.error.message" />
    <g:each var='error' in="${validationErrors}" >
        <li> ${error} </li>
    </g:each>
</ul>