<%--
  Created by IntelliJ IDEA.
  User: relango
  Date: 9/09/12
  Time: 10:59 PM
  To change this template use File | Settings | File Templates.
--%>
<table class="table table-bordered">
    <thead>
    <tr>
        <th>
            <g:message code='${labelMessageCode}'/>&nbsp<g:message code='features.label'/>
        </th>
        <th>
            <g:message code='supported.label'/>
        </th>
    </tr>
    </thead>
    <tbody>
    <g:each in="${compatibilityList}" var="i">
        <tr>
            <td>
                ${i.feature}
            </td>
            <td class="center">
                <g:if test="${i.supported}">
                    <g:img dir="images" file="check-24.png"/>
                </g:if>
                <g:else>
                    <g:img dir="images" file="cross-24.png"/>
                </g:else>
            </td>
        </tr>
    </g:each>
    </tbody>
</table>