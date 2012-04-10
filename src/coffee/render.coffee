window.updateRenderedItem = (data) ->
  if data.redirectUrl
    $.post(data.redirectUrl, (htmlResp) ->
        $('html').html(htmlResp)
        return
    )
  else
    if data.responseValues
      for name,value of data.responseValues
        $("input[name = " + name + "]").val(value)
        ;
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
