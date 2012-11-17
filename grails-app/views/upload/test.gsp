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
    <r:require module="fileupload"/>
    <title><g:message code="upload.label"/>&nbsp;<g:message code="test.label"/></title>
    <style type="text/css" media="screen">

    .bar {
        height: 18px;
        background: green;
    }
    </style>
</head>

<body>
<input id="fileupload" type="file" name="files[]" data-url="server/php/" multiple>


<div class="row-fluid">
    <div id="progress">
        <div class="bar" style="width: 0%;"></div>
    </div>
</div>

<script>
    $(function () {
        $('#fileupload').fileupload({
            dataType: 'json',
            done: function (e, data) {
                $.each(data.result, function (index, file) {
                    $('<p/>').text(file.name).appendTo(document.body);
                });
            }
        });
    });
    $('#fileupload').fileupload({
        progressall: function (e, data) {
            var progress = parseInt(data.loaded / data.total * 100, 10);
            $('#progress .bar').css(
                    'width',
                    progress + '%'
            );
        }
    });

</script>
</body>
</html>