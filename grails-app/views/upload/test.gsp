<%--
  Created by IntelliJ IDEA.
  User: relango
  Date: 11/17/12
  Time: 12:22 AM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html xmlns:m="http://www.w3.org/1998/Math/MathML">
<head>
    <meta name="layout" content="primary"/>
    <r:require modules="fileupload"/>
    <title><g:message code="upload.label"/>&nbsp;<g:message code="test.label"/></title>

</head>

<body>
<div class="row-fluid">
    <div class="span3">&nbsp;</div>

    <div class="span6">
        <uploadr:add name="testUploader" path="c:/Raja/projects/rialms/dev/rialms"
                     viewable="false"
                     downloadable="false"
                     placeholder="Or Drop it here"
                     fileselect="Browse to upload file"
                     deletable="false"
                     class="fileupload"
            maxSize="10000000"
                     maxVisible="1"/>
    </div>
</div>
</body>
</html>