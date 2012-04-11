$ = jQuery
window.updateRenderedItem = (data) ->
  if data.redirectUrl
    $('html').load(data.redirectUrl, data.params, ->
        $.getScript("http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML")
        alert('done')
    )
    return
  else
    if data.responseValues
      for name,value of data.responseValues
        $("input[name = " + name + "]:not(:button)").val(value)
        $('form').field(name,value)
    if data.visibleElementIds
      $(visibleElementId).show() for visibleElementId in data.visibleElementIds
    if data.hiddenElementIds
      $(hiddenElementId).hide() for hiddenElementId in data.hiddenElementIds
    if data.outcomeValues
      outcomeValuesText = JSON.stringify(data.outcomeValues)
      $('#itemOutcomeValues').text(outcomeValuesText)
    if data.disableElementIds
      $(disableElementId).attr("disabled", true) for disableElementId in data.disableElementIds
  return

$.fn.field = (inputName, value) ->
  return false  if typeof inputName isnt "string"
  $inputElement = $(this).find("[name=" + inputName + "]")
  console.log $inputElement
  switch $inputElement.attr("type")
    when "checkbox"
      $inputElement.each (i) ->
        checked = $.inArray($(this).val(), value.split(",")) >= 0
        $(this).attr('checked',checked)
    when "radio"
      $inputElement.each (i) ->
        $(this).attr('checked',true)  if $(this).val() is value
    when "select"
      $inputElement.each (i) ->
        console.log $(this)
        selected = $(this).val() is value
        $(this).attr('selected',selected)
    when "button"
      break;
    when "submit"
      break
    when `undefined`
      $(this).append "<input type=\"hidden\" name=\"" + inputName + "\" value=\"" + value + "\" />"
    else
      console.log("warning unhandled element " + $inputElement );
      $inputElement.val value
  $inputElement


