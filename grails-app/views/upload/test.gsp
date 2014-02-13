<%--
  Created by IntelliJ IDEA.
  User: relango
  Date: 11/17/12
  Time: 12:22 AM
  To change this template use File | Settings | File Templates.
--%>

<%@ page import="com.rialms.consts.Constants;" contentType="text/html;charset=UTF-8" %>
<html xmlns:m="http://www.w3.org/1998/Math/MathML">
<head>
    <meta name="layout" content="primary"/>
    <r:require modules="fileupload"/>
    <title><g:message code="upload.label"/>&nbsp;<g:message code="test.label"/></title>

</head>
<body>
<div class="row-fluid" id="uploadDiv" ng-controller="UploadController">
    <!-- <div class="span6">&nbsp;</div> -->

    <div class="span12">
        <uploadr:add name="testUploader" path="${uploadDir}"
                     viewable="false"
                     downloadable="false"
                     placeholder="${g.message(code: 'upload.dropable.placeholder')}"
                     fileselect="${g.message(code: 'button.upload.fileselect.label')}"
                     deletable="false"
                     class="fileupload"
                     maxVisible="1">
            <uploadr:onSuccess>
                ${g.remoteFunction(controller:"upload", action:"saveUploadedTest", params:'\'uploadedFile=\' + file.fileName', onSuccess:'window.updateUploadResult(data)')}
            </uploadr:onSuccess>
        </uploadr:add>
    </div>
    <div>${testsByCurrentUser}</div>
    <div>{{uploadResult}}</div>

</div>
</body>
</html>