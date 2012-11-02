# JQuery Validation UI for Twitter Bootstrap
window.highlightErrorField = (element, errorClass, validClass) ->
  $(element).parents("div.control-group").addClass errorClass

window.unhighlightErrorField = (element, errorClass, validClass) ->
  $(element).parents("div.control-group").removeClass errorClass

window.disableValidationRules = (formId) ->
  $(formId).find(":input").each ->
    $(this).rules "remove"