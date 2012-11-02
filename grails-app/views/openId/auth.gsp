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
    <div class="span4">&nbsp;</div>
    <g:hasErrors bean="${command}">    <!--TODO find better way for errors -->
        <div class="errors">
            <g:renderErrors bean="${command}" as="list"/>
        </div>
    </g:hasErrors>
    <div class="span4" ng-controller='LoginController'>
        <!-- TODO p4 Figure out how to do i18n with augularJs -->
        <div id="login">
            <div class="block-header">
                <h4 ng-init="isSignUp=${isSignUp? isSignUp: false}">{{getTitle()}}
                    <span class="pull-right">
                        {{getMessage()}}&nbsp;
                        <button ng-click='toggleForms()' class="btn btn-danger btn-mini">{{getButtonLabel()}}</button>
                        &nbsp;&nbsp;
                    </span>
                </h4>
            </div>

            <div class="block-content">
              <!--  <g:if test='${flash.message}'>
                    <div class='item-result result-incorrect '>${flash.message}</div>
                </g:if>   -->
                <div id='openidLogin' ng-hide="isSignUp">
                    <form class="form-horizontal" action='${daoPostUrl}' id='loginForm' method='POST'
                          autocomplete='off'>
                        <fieldset>

                            <div class="${flash.message ? 'control-group error' : 'control-group'}">
                                <label class="control-label" for="email"><g:message code='email.label'/></label>

                                <div class="controls">
                                    <input type="email" placeholder="${g.message(code:'your.label')} ${g.message(code:'email.label')}" class="input-large"
                                           name='j_username' id='email'>
                                    <g:if test='${flash.message}'>
                                        <label for='email' class="error">${flash.message}</label>
                                    </g:if>
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
                                    <input type="hidden" name="${openidIdentifier}"
                                           value="${openIdIdentifierValue}">
                                    <button type='submit' formaction="${openIdPostUrl}" class="zocial google"  onclick="disableValidationRules('#loginForm')" ><g:message
                                          code='button.loginWithGoogle.label'/></button>
                                </div>
                            </div>

                        </fieldset>
                        <!-- TODO P1: do forgot password -->
                    </form>
                </div>

                <div ng-show="isSignUp">
                    <g:form action='signUpAccount' controller='OpenId' class="form-horizontal" method='POST' autocomplete='off' name="signUpForm" >
                        <fieldset>
                            <% boolean emailHasError = hasErrors(bean: command, field: 'email', 'errors') %>
                            <div class="${emailHasError ? 'control-group error' : 'control-group'}">
                                <label class="control-label" for="email"><g:message code='email.label'/></label>

                                <div class="controls">
                                    <g:textField type="text" placeholder="${g.message(code:'your.label')} ${g.message(code:'email.label')}"
                                                 class="input-large"
                                                 name='email' value="${command.email}"/>
                                    <g:if test='${emailHasError}'>
                                        <g:eachError bean="${command}" field="email">
                                            <label for='email' class="error"><g:message error="${it}" /></label>
                                        </g:eachError>
                                    </g:if>
                                </div>
                            </div>

                            <div class="control-group">
                                <label class="control-label" for="name"><g:message code='username.label'/></label>

                                <div class="controls">
                                    <g:textField type="text" placeholder="${g.message(code:'your.label')} ${g.message(code:'username.label')}"
                                                 class="input-large"
                                                 name='name' value="${command.name}" />
                                </div>
                            </div>
                            <% boolean passwordHasError = hasErrors(bean: command, field: 'password', 'errors') %>
                            <div class="${passwordHasError ? 'control-group error' : 'control-group'}">
                                <label class="control-label" for="password"><g:message code='password.label'/></label>

                                <div class="controls">
                                    <g:passwordField type="text"
                                                 class="input-large"
                                                 name='password' value="${command.password}"/>
                                    <g:if test='${passwordHasError}'>
                                        <g:eachError bean="${command}" field="password">
                                            <label for='password' class="error"><g:message error="${it}" /></label>
                                        </g:eachError>
                                    </g:if>
                                </div>
                            </div>

                            <div class="control-group">
                                <div class="controls">
                                    <input type='submit' class="btn btn-info" value="${g.message(code:'button.signup.label')}"/>
                                    <span>&nbsp; <g:message code="or.label"/> &nbsp;</span>
                                    <input type="hidden" name="${openidIdentifier}"
                                           value="${openIdIdentifierValue}">
                                    <button type='submit' formaction="${openIdPostUrl}" class="zocial google" onclick="disableValidationRules('#signUpForm')"><g:message
                                            code='button.signupWithGoogle.label'/></button>
                                </div>
                            </div>
                        </fieldset>
                    </g:form>
                </div>

            </div>

        </div>
    </div>
</div>
<jqvalui:renderValidationScript for="OpenIdRegisterCommand" form="signUpForm" validClass="success"  />
<jqvalui:renderValidationScript for="LoginCommand" form="loginForm" validClass="success"  />
<script>
    $(document).ready(function () {
        $('#email').focus();
    });
</script>
</body>