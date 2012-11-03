<%--
  Created by IntelliJ IDEA.
  User: relango
  Date: 11/03/12
  Time: 11:03 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="primary"/>
    <r:require modules="zocial,jquery-validation-ui"/>
    <title><g:message code="project.name"/> - <g:message code='spring.security.ui.forgotPassword.title'/></title>
</head>

<body>
<div class="documentation row-fluid">
    <div class="span4">&nbsp;</div>

    <div class="span4">
        <g:form action='forgotPassword' name="forgotPasswordForm" class="form-horizontal" autocomplete='off'>
            <fieldset>
                <g:if test='${emailSent}'>
                    <a class="close" data-dismiss="alert" href="#">&times;</a>
                    <div class="alert alert-block">
                       <h4><g:message code='forgotPassword.sent'/></h4>
                    </div>
                </g:if>
                <g:else>
                    <div class="block-header">
                        <h4><g:message code="forgotPassword.header"/></h4>
                    </div>

                    <div class="block-content">
                        <% boolean hasError = hasErrors(bean: command, field: 'email', 'errors') %>
                        <div class="${hasError ? 'control-group error' : 'control-group'}">
                            <label class="control-label" for="email"><g:message code='email.label'/></label>

                            <div class="controls">
                                <g:textField name="email" size="25"/>
                                <g:if test='${hasError}'>
                                    <g:eachError bean="${command}" field="email">
                                        <label for='email' class="error"><g:message error="${it}"/></label>
                                    </g:eachError>
                                </g:if>
                            </div>
                        </div>

                        <div class="control-group">
                            <div class="controls">
                                <button type="submit" class="btn btn-info"><g:message
                                        code='button.forgotPassword.label'/></button>
                            </div>
                        </div>
                    </div>
                </g:else>
            </fieldset>
        </g:form>
    </div>
</div>
<jqvalui:renderValidationScript for="ForgotPasswordCommand"/>
</body>
</html>