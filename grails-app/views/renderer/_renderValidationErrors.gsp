<%--
  Created by IntelliJ IDEA.
  User: Rajasekar Elango
  Date: 04/02/12
  Time: 05:20 PM
  To change this template use File | Settings | File Templates.
--%>
<% String alertClass = validationResult.errors.isEmpty() ? 'alert alert-warning' :'alert alert-error' %>
<div class="${alertClass}">
    <a class="close" data-dismiss="alert" href="#">&times;</a>
    <dl>
        <dt> <g:message code="item.validation.error.message"/> </dt>
        <g:each var='error' in="${validationResult.allItems}">
            <dd>${error}</dd>
        </g:each>
    </dl>
</div>