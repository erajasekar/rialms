<%--
  Created by IntelliJ IDEA.
  User: relango
  Date: 11/4/12
  Time: 11:41 PM
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
<div class="row-fluid">

    <div class="auth-box">

        <g:if test='${flash.error}'>
            <rs:alertMsg messageCode="${flash.error}"/>
        </g:if>
        <g:elseif test='${passwordReset}'>
            <a class="close" data-dismiss="alert" href="#">&times;</a>

            <div class="alert alert-block">
                <h4><g:message code='resetPassword.success.message'/></h4>
            </div>
        </g:elseif>
        <g:else>
            <div class="block-header">
                <h4><g:message code="resetPassword.header"/></h4>
            </div>

            <div class="block-content">
                <g:form action='resetPassword' name="resetPasswordForm" class="form-inline" autocomplete='off'>
                    <g:hiddenField name='t' value='${token}'/>
                    <fieldset>
                        <% boolean passwordHasError = hasErrors(bean: command, field: 'password', 'errors') %>
                        <div class="${passwordHasError ? 'control-group error' : 'control-group'}">

                            <div class="controls">
                                <div class="input-prepend">
                                    <span class="add-on"><i class="icon-lock"></i></span><g:passwordField type="text"
                                                                                                          class="input-large"
                                                                                                          placeholder="${g.message(code: 'new.label')} ${g.message(code: 'password.label')}"
                                                                                                          name='password'
                                                                                                          value="${command?.password}"/>
                                    <g:if test='${passwordHasError}'>
                                        <g:eachError bean="${command}" field="password">
                                            <label for='password' class="error"><g:message error="${it}"/></label>
                                        </g:eachError>
                                    </g:if>
                                </div>
                            </div>
                        </div>
                        <hr/>
                        <div class="control-group">
                            <div class="controls">
                                <button type="submit" class="btn btn-success"><g:message
                                        code='button.resetPassword.label'/></button>
                                <span class="pull-right">
                                    <a href="${createLink(controller: 'openId', action: 'auth')}"
                                       class="btn btn-info"><g:message
                                            code='button.cancel.label'/></a>
                                </span>
                            </div>
                        </div>
                    </fieldset>
                </g:form>
            </div>
        </g:else>
    </div>

</div>
<jqvalui:renderValidationScript for="ResetPasswordCommand"/>
</body>
</html>
