<%--
  Created by IntelliJ IDEA.
  User: relango
  Date: 4/11/12
  Time: 4:02 PM
  To change this template use File | Settings | File Templates.
--%>
<!doctype html>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <meta name="layout" content="primary"/>
  <title>Test field query function</title>
  <r:script>
   // $('body').field('text','text1');
   $('body').field('check','A,C');
   $('body').field('checksingle','A');
   $('body').field('radio','C');
   $('body').field('select','B');
 //   $('body').field('button','new val');
 //   $('body').field('submit','new val');
  </r:script>
</head>
<body>
     <br/>
     <div>
         Text : <g:textField name="text" /> (should have value text1)
     </div>
     <div> Checkbox group
        <g:checkBox name="check" value='A'/> A
        <g:checkBox name="check" value='B'/> B
        <g:checkBox name="check" value='C'/> C
       (first and last should be checked. )
    </div>
    <div> Checkbox
        <g:checkBox name="checksingle" value='A' checked="false" /> A
        (should be checked. )
    </div>

    <div>Radio
        <g:radio name="radio" value='A'/> A
        <g:radio name="radio" value='B'/> B
        <g:radio name="radio" value='C'/> C
        (only last should be checked. )
    </div>
    <div>Select
        <g:select name="select" from="['A','B','C']" />
        (only B should be selected. )
    </div>
    <div> Button
        <input type="button" name="button" value="button"/> (shouldn't be affected)
    </div>
    <div> Button
       <g:submitButton name="submit" value="submit"/> (shouldn't be affected)
    </div>

</body>
</html>