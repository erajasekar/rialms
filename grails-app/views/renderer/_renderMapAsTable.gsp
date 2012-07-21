
<%--
  Created by IntelliJ IDEA.
  User: Rajasekar Elango
  Date: 04/24/12
  Time: 10:48 PM
  To change this template use File | Settings | File Templates.
--%>
<table class="table table-bordered table-striped">
    <g:each var='entry' in="${mapTableData}">
        <tr>
            <td>${entry.key}</td>
            <td>${entry.value}</td>
        </tr>
    </g:each>
</table>