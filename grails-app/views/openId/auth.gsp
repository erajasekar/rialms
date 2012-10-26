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
    <r:require module="zocial"/>
    <title><g:message code="project.name"/> - <g:message code="home.label"/></title>
</head>

<body>
<div class="documentation row-fluid">
    <div class="span4">&nbsp;</div>

    <div class="span4" ng-controller='LoginController'>
        <!-- TODO p4 Figure out how to do i18n with augularJs -->
        <div id="login">
            <div class="block-header">
                <h4 ng-init="title='Login';isSignUp=false;message='Need an account?';buttonLabel='Sign Up'">{{title}}
                    <span class="pull-right">
                        {{message}}&nbsp;
                        <button ng-click='toggleForms()' class="btn btn-danger btn-mini">{{buttonLabel}}</button>
                        &nbsp;&nbsp;
                    </span>
                </h4>
            </div>

            <div class="block-content">
                <g:if test='${flash.message}'>
                    <div class='item-result result-incorrect '>${flash.message}</div>
                </g:if>
                <div id='openidLogin' ng-hide="isSignUp">
                    <form class="form-horizontal" action='${daoPostUrl}' id='loginForm' method='POST'
                          autocomplete='off'>
                        <fieldset>

                            <div class="control-group">
                                <label class="control-label" for="email"><g:message code='email.label'/></label>

                                <div class="controls">
                                    <input type="text" placeholder="${g.message(code:'your.label')} ${g.message(code:'email.label')}" class="input-large"
                                           name='j_username' id='email'>
                                </div>
                            </div>

                            <div class="control-group">
                                <label class="control-label" for="login_password"><g:message
                                        code='password.label'/></label>

                                <div class="controls">
                                    <input type="password"
                                           class="input-large" name='j_password' id='login_password'>
                                </div>
                            </div>

                            <div class="control-group">
                                <div class="controls">
                                    <label class="checkbox">
                                        <input type='checkbox' name='${rememberMeParameter}' id='remember_me'><g:message
                                            code='rememberme.label'/> </input>
                                    </label>
                                </div>
                            </div>

                            <div class="control-group">
                                <div class="controls">
                                    <button type="submit" class="btn btn-info"><g:message
                                            code='button.login.label'/></button>
                                    <span>&nbsp; or &nbsp;</span>
                                    <!-- TODO: P3: WILL WORK ONLY ON HTML5 -->
                                    <!-- TODO: P1: add i18n and validation for empty -->
                                    <input type="hidden" name="openid_identifier"
                                           value="https://www.google.com/accounts/o8/id">
                                    <button type='submit' formaction="${openIdPostUrl}" class="zocial google"><g:message
                                            code='button.loginWithGoogle.label'/></button>
                                </div>
                            </div>

                        </fieldset>
                        <!-- TODO P1: do forgot password -->
                    </form>
                </div>

                <div id='formLogin' ng-show="isSignUp">
                    <form action='${daoPostUrl}' class="form-horizontal" method='POST' autocomplete='off'>
                        <fieldset>
                            <div class="control-group">
                                <label class="control-label" for="email"><g:message code='email.label'/></label>

                                <div class="controls">
                                    <g:textField type="text" placeholder="${g.message(code:'your.label')} ${g.message(code:'email.label')}"
                                                 class="input-large"
                                                 name='email' />
                                </div>
                            </div>

                            <div class="control-group">
                                <label class="control-label" for="username"><g:message code='username.label'/></label>

                                <div class="controls">
                                    <g:textField type="text" placeholder="${g.message(code:'your.label')} ${g.message(code:'username.label')}"
                                                 class="input-large"
                                                 name='username' />
                                </div>
                            </div>

                            <div class="control-group">
                                <label class="control-label" for="password"><g:message code='password.label'/></label>

                                <div class="controls">
                                    <g:textField type="text"
                                                 class="input-large"
                                                 name='password' />
                                </div>
                            </div>

                            <div class="control-group">
                                <div class="controls">
                                    <input type='submit' class="btn btn-info" value="${g.message(code:'button.signup.label')}"/>
                                    <span>&nbsp; <g:message code="or.label"/> &nbsp;</span>
                                    <input type="hidden" name="openid_identifier"
                                           value="https://www.google.com/accounts/o8/id">
                                    <button type='submit' formaction="${openIdPostUrl}" class="zocial google"><g:message
                                            code='button.signupWithGoogle.label'/></button>
                                </div>
                            </div>
                        </fieldset>
                    </form>
                </div>

            </div>

        </div>
    </div>
</div>
<script>
    $(document).ready(function () {
        $('#email').focus();
    });
</script>