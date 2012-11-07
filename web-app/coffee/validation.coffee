# JQuery Validation UI for Twitter Bootstrap
window.highlightErrorField = (element, errorClass, validClass) ->
  $(element).parents("div.control-group").addClass errorClass

window.unhighlightErrorField = (element, errorClass, validClass) ->
  $(element).parents("div.control-group").removeClass errorClass

window.disableValidationRules = (formId) ->
  $(formId).find(":input").each ->
    $(this).rules "remove"

window.showFlash = (type, text, duration) ->
  clazz = "icon "
  if type is "error"
    clazz += "icon_error"
  else clazz += "icon_info"  if type is "info"
  $.jGrowl "<span class=\"" + clazz + "\">" + text + "</span>",
    life: duration