<%--
  Created by IntelliJ IDEA.
  User: Rajasekar Elango
  Date: 8/1/12
  Time: 11:48 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.rialms.consts.Constants as Consts" contentType="text/html;charset=UTF-8" %>
<span ng-show='!${Consts.isResponseValid}'>
    <span class="alert item-result result-incorrect">
        <g:message code="response.invalid.message"/>
    </span>
</span>
