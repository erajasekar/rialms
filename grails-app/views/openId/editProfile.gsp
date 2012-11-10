<%--
  Created by IntelliJ IDEA.
  User: relango
  Date: 11/10/12
  Time: 12:05 AM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="primary"/>
    <r:require modules="zocial,jquery-validation-ui"/>
    <title><g:message code="project.name"/> - <g:message code='editprofile.label'/></title>
</head>

<body>
<div class="row-fluid">
    <div class="auth-box">
        <div class="block-header">
            <h4><g:message code="editprofile.label"/></h4>
        </div>
        <%=  command.errors %>
        <div class="block-content">
            <form action="editProfile" class="form-horizontal" id='editProfileForm' method='POST'>
                <fieldset>
                    <div class="control-group">
                        <label class="control-label" for="email_login"><g:message
                                code='email.label'/></label>

                        <div class="controls">
                            <div class="input-prepend">
                                <span class="add-on"><i class="icon-envelope"></i></span><input type="email"
                                                                                                class="input-large  uneditable-input"
                                                                                                placeholder="${g.message(code: 'email.label')}"
                                                                                                name='email'
                                                                                                value="${command.email}"
                                                                                                disabled="true"
                                                                                                id='email_login'/>
                            </div>
                        </div>
                    </div>
                    <br/>
                    <% boolean nameHasError = hasErrors(bean: command, field: 'displayName', 'errors') %>
                    <div class="${nameHasError ? 'control-group error' : 'control-group'}">
                        <label class="control-label" for="displayName"><g:message
                                code='username.label'/></label>

                        <div class="controls">
                            <div class="input-prepend">
                                <span class="add-on"><i class="icon-user"></i></span><g:textField
                                    type="text"
                                    placeholder="${g.message(code: 'username.label')}"
                                    class="input-large"
                                    name='displayName' value="${command.displayName}"/>
                                <g:if test='${nameHasError}'>
                                    <g:eachError bean="${command}" field="displayName">
                                        <label for='displayName' class="error"><g:message
                                                error="${it}"/></label>
                                    </g:eachError>
                                </g:if>
                            </div>
                        </div>
                    </div>
                    <br/>
                    <% boolean newPasswordHasError = hasErrors(bean: command, field: 'newPassword', 'errors') %>
                    <div class="${newPasswordHasError ? 'control-group error' : 'control-group'}">
                        <label class="control-label" for="newPassword"><g:message
                                code='new.label'/>&nbsp;<g:message
                                code='password.label'/></label>

                        <div class="controls">
                            <div class="input-prepend">
                                <span class="add-on"><i class="icon-lock"></i></span><g:passwordField
                                    type="text"
                                    placeholder="${g.message(code: 'new.label')} ${g.message(code: 'password.label')} ${g.message(code: 'tochange.label')}"
                                    class="input-large"
                                    name='newPassword' value="${command.newPassword}"/>
                                <g:if test='${newPasswordHasError}'>
                                    <g:eachError bean="${command}" field="newPassword">
                                        <label for='newPassword' class="error"><g:message
                                                error="${it}"/></label>
                                    </g:eachError>
                                </g:if>
                            </div>
                        </div>
                    </div>
                    <br/>
                    <% boolean currentPasswordHasError = hasErrors(bean: command, field: 'currentPassword', 'errors') %>
                    <div class="${currentPasswordHasError ? 'control-group error' : 'control-group'}">
                        <label class="control-label" for="currentPassword"><g:message
                                code='current.label'/>&nbsp;<g:message
                                code='password.label'/></label>

                        <div class="controls">
                            <div class="input-prepend">
                                <span class="add-on"><i class="icon-lock"></i></span><g:passwordField
                                    type="text"
                                    placeholder="${g.message(code: 'current.label')} ${g.message(code: 'password.label')} ${g.message(code: 'toauthenticate.label')}"
                                    class="input-large"
                                    name='currentPassword' value="${command.currentPassword}"/>
                                <g:if test='${currentPasswordHasError}'>
                                    <g:eachError bean="${command}" field="currentPassword">
                                        <label for='currentPassword' class="error"><g:message
                                                error="${it}"/></label>
                                    </g:eachError>
                                </g:if>
                            </div>
                        </div>
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="btn btn-success"><g:message
                                code='button.save.label'/></button>
                        <span>&nbsp;&nbsp;&nbsp;&nbsp;</span>
                        <a href="${createLink(controller: 'openId', action: 'auth')}"
                           class="btn btn-info"><g:message
                                code='button.cancel.label'/></a>
                    </div>
                </fieldset>
            </form>
        </div>
    </div>
</div>
<jqvalui:renderValidationScript for="EditProfileCommand"/>
</body>
</html>