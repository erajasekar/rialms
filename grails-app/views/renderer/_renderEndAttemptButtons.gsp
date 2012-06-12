<%--
  Created by IntelliJ IDEA.
  User: Rajasekar Elango
  Date: 3/23/12
  Time: 11:48 PM
  To change this template use File | Settings | File Templates.
--%>
<g:each var="endAttemptButton" in="${assessmentItemInfo.headerButtons}">
    <qti:endAttemptButton buttonIdentifier="${endAttemptButton.key}" buttonTitle="${endAttemptButton.value}" />
</g:each>
