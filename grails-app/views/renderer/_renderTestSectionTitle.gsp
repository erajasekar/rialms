<%@ page import="com.rialms.consts.Constants as Consts" %>
<%--
  Created by IntelliJ IDEA.
  User: Rajasekar Elango
  Date: 5/17/12
  Time: 11:55 PM
  To change this template use File | Settings | File Templates.
--%>
<div id="${Consts.testSectionTitleContent}" class="row-fluid">
    <div class="breadcrumb">
         <qti:assessmentSection sectionTitles="${assessmentParams[Consts.sectionTitles]}"/>
    </div>
</div>