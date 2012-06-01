<%--
  Created by IntelliJ IDEA.
  User: relango
  Date: 06/01/12
  Time: 3:17 PM
  To change this template use File | Settings | File Templates.
--%>
<div class="form-actions">
    <g:link action="report" params="[id: params.id]" class="btn btn-primary" target="_blank"><i
            class="icon-signal icon-white"></i> Report</g:link>

    <g:link name='exit' action="reset" params="[id: params.id, redirectto: 'list']"
            class="btn btn-danger"
            onclick="return confirm(\'${g.message(code: 'test.exit.confirm.message')}\')"><i
            class="icon-remove icon-white"></i> Exit Test</g:link>
</div>