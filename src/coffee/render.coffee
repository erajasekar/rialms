$ = jQuery
window.updateRenderedItem = (data) ->
  if data.redirectUrl
    $.post(data.redirectUrl, (resp) ->
        document.open()
        document.write(resp)
        document.close()
    )
    return
  else
    if data.responseValues
      for name,value of data.responseValues
        $('form').field(name, value)
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
  switch $inputElement.attr("type")
    when "checkbox"
      $inputElement.each (i) ->
        checked = $.inArray($(this).val(), value.split(",")) >= 0
        $(this).attr('checked', checked)
    when "radio"
      $inputElement.each (i) ->
        $(this).attr('checked', true)  if $(this).val() is value
    when "button"
      break;
    when "submit"
      break
    when `undefined`
      switch $inputElement.attr("id")
        when "select"
          $inputElement.children('option').each (i) ->
            $(this).attr('selected', 'selected') if $(this).val() is value
        else
          $(this).append "<input type=\"hidden\" name=\"" + inputName + "\" value=\"" + value + "\" />"
    else
    #console.log("warning unhandled element " + $inputElement)
      $inputElement.val value
  $inputElement


