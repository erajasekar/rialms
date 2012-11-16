# JQuery Validation UI for Twitter Bootstrap
window.highlightErrorField = (element, errorClass, validClass) ->
  $(element).parents("div.control-group").addClass errorClass

window.unhighlightErrorField = (element, errorClass, validClass) ->
  $(element).parents("div.control-group").removeClass errorClass

window.disableValidationRules = (formId) ->
  $(formId).find(":input").each ->
    $(this).rules "remove"

window.showFlash = (type, text, duration) ->
  console.log(type);
  if type is "error"
    iconClass = "icon-remove"
    spanClass = "result-incorrect"
  else
    if type is "info"
      iconClass = "icon-ok"
      spanClass = "result-correct"
  $.jGrowl "<span class=\"" + spanClass + "\"><i class=\"" + iconClass + "\">&nbsp;</i>" + text + "</span>",
    life: duration
