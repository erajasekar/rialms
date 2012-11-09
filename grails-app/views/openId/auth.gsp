<%--
  Created by IntelliJ IDEA.
  User: relango
  Date: 10/18/12
  Time: 11:03 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="primary"/>
    <r:require modules="zocial,jquery-validation-ui"/>
    <title><g:message code="project.name"/> - <g:message code="home.label"/></title>
</head>

<body>
<div class="documentation row-fluid">
    <div class="auth-box" ng-controller='LoginController'>

        <g:if test='${emailSent}'>
            <rs:alertMsg messageCode='register.success.message'/>
        </g:if>
        <g:else>
            <!-- TODO p4 Figure out how to do i18n with augularJs -->
            <div id="login">
                <div class="block-header">
                    <h4 ng-init="isSignUp=${isSignUp ? isSignUp : false}">{{getTitle()}}
                        <span class="pull-right">
                            {{getMessage()}}&nbsp;
                            <button ng-click='toggleForms()'
                                    class="btn btn-info btn-mini">{{getButtonLabel()}}</button>
                            &nbsp;&nbsp;
                        </span>
                    </h4>
                </div>

                <div class="block-content">
                    <div id='openidLogin' ng-hide="isSignUp">
                        <form class="form-inline" action='${daoPostUrl}' id='loginForm' method='POST'>
                            <fieldset>
                                <div class="${flash.message ? 'control-group error' : 'control-group'}">
                                    <!--      <label class="control-label" for="email"><g:message
                                            code='email.label'/></label> -->
                                    <div class="controls">
                                        <div class="input-prepend">
                                            <span class="add-on"><i class="icon-envelope"></i></span><input type="email"
                                                                                                            class="input-large"
                                                                                                            placeholder="${g.message(code: 'your.label')} ${g.message(code: 'email.label')}"
                                                                                                            name='j_username'
                                                                                                            id='email'/>
                                            <g:if test='${flash.message}'>
                                                <label for='email' class="error">${flash.message}</label>
                                            </g:if>
                                        </div>
                                    </div>

                                </div>
                                <br/>

                                <div class="control-group">
                                    <!--    <label class="control-label" for="login_password"><g:message
                                            code='password.label'/></label>       -->

                                    <div class="controls">
                                        <div class="input-prepend">
                                            <span class="add-on"><i class="icon-lock"></i></span><input type="password"
                                                                                                        class="input-large"
                                                                                                        placeholder="${g.message(code: 'your.label')} ${g.message(code: 'password.label')}"
                                                                                                        name='j_password'
                                                                                                        id='login_password'>
                                        </div>
                                    </div>
                                </div>

                                <div class="control-group">
                                    <div class="controls">
                                        <label class="checkbox">
                                            <input type='checkbox' name='${rememberMeParameter}'
                                                   id='remember_me'><g:message
                                                code='rememberme.label'/> </input>
                                        </label>
                                    </div>
                                </div>
                                <hr/>

                                <div class="control-group">
                                    <div class="controls">
                                        <button type="submit" class="btn btn-success"><g:message
                                                code='button.login.label'/></button>
                                        <span>&nbsp; or &nbsp;</span>
                                        <!-- TODO: P3: WILL WORK ONLY ON HTML5 -->
                                        <input type="hidden" name="${openidIdentifier}"
                                               value="${openIdIdentifierValue}">
                                        <button type='submit' formaction="${openIdPostUrl}" class="zocial google"
                                                onclick="disableValidationRules('#loginForm')"><g:message
                                                code='button.loginWithGoogle.label'/></button>
                                        <span class="pull-right">
                                            <g:link controller="openId" action="forgotPassword"><g:message
                                                code="forgotPassword.message"/>
                                            </g:link>
                                        </span>
                                    </div>
                                </div>
                            </fieldset>
                        </form>
                    </div>

                    <div ng-show="isSignUp">
                        <g:form class="form-inline" action='signUpAccount' controller='OpenId' method='POST' name="signUpForm">
                            <fieldset>
                                <% boolean emailHasError = hasErrors(bean: command, field: 'email', 'errors') %>
                                <div class="${emailHasError ? 'control-group error' : 'control-group'}">
                                    <!--     <label class="control-label" for="email"><g:message
                                            code='email.label'/></label>  -->

                                    <div class="controls">
                                        <div class="input-prepend">
                                            <span class="add-on"><i class="icon-envelope"></i></span><g:textField
                                                type="text"
                                                placeholder="${g.message(code: 'your.label')} ${g.message(code: 'email.label')}"
                                                class="input-large"
                                                name='email' value="${command.email}"/>
                                            <g:if test='${emailHasError}'>
                                                <g:eachError bean="${command}" field="email">
                                                    <label for='email' class="error"><g:message error="${it}"/></label>
                                                </g:eachError>
                                            </g:if>
                                        </div>
                                    </div>
                                </div>
                                <br/>

                                <div class="control-group">
                                    <!--      <label class="control-label" for="name"><g:message
                                            code='username.label'/></label>   -->

                                    <div class="controls">
                                        <div class="input-prepend">
                                            <span class="add-on"><i class="icon-user"></i></span><g:textField
                                                type="text"
                                                placeholder="${g.message(code: 'your.label')} ${g.message(code: 'username.label')}"
                                                class="input-large"
                                                name='displayName' value="${command.displayName}"/>
                                        </div>
                                    </div>
                                </div>
                                <br/>
                                <% boolean passwordHasError = hasErrors(bean: command, field: 'password', 'errors') %>
                                <div class="${passwordHasError ? 'control-group error' : 'control-group'}">
                                    <!--     <label class="control-label" for="password"><g:message
                                            code='password.label'/></label>         -->

                                    <div class="controls">
                                        <div class="input-prepend">
                                            <span class="add-on"><i class="icon-lock"></i></span><g:passwordField
                                                type="text"
                                                class="input-large"
                                                placeholder="${g.message(code: 'your.label')} ${g.message(code: 'password.label')}"
                                                name='password' value="${command.password}"/>
                                            <g:if test='${passwordHasError}'>
                                                <g:eachError bean="${command}" field="password">
                                                    <label for='password' class="error"><g:message
                                                            error="${it}"/></label>
                                                </g:eachError>
                                            </g:if>
                                        </div>
                                    </div>
                                </div>
                                <hr/>

                                <div class="control-group">
                                    <div class="controls">
                                        <input type='submit' class="btn btn-success"
                                               value="${g.message(code: 'button.signup.label')}"/>
                                        <span>&nbsp; <g:message code="or.label"/> &nbsp;</span>
                                        <input type="hidden" name="${openidIdentifier}"
                                               value="${openIdIdentifierValue}">
                                        <button type='submit' formaction="${openIdPostUrl}" class="zocial google"
                                                onclick="disableValidationRules('#signUpForm')"><g:message
                                                code='button.signupWithGoogle.label'/></button>
                                    </div>
                                </div>
                            </fieldset>
                        </g:form>
                    </div>

                </div>

            </div>
        </g:else>
    </div>
</div>
<jqvalui:renderValidationScript for="OpenIdRegisterCommand" form="signUpForm"/>
<jqvalui:renderValidationScript for="LoginCommand" form="loginForm"/>
<g:javascript src='jquery/jquery.jgrowl.js' plugin='spring-security-ui'/>
<g:javascript src='spring-security-ui.js' plugin='spring-security-ui'/>
<script>
    $(document).ready(function () {
        $('#email').focus();
    });
</script>
</body>