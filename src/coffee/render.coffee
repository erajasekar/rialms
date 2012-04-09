window.updateRenderedItem = (data) ->
  $('#moveon').click() if (data.redirectUrl)
  if data.visibleElementIds
    $(visibleElementId).show() for visibleElementId in data.visibleElementIds
    ;
  if data.hiddenElementIds
    $(hiddenElementId).hide() for hiddenElementId in data.hiddenElementIds
    ;
  if data.outcomeValues
    outcomeValuesText = JSON.stringify(data.outcomeValues)
    ;
    $('#itemOutcomeValues').text(outcomeValuesText)
    ;
  if data.disableElementIds
    $(disableElementId).attr("disabled", true) for disableElementId in data.disableElementIds
    ;
  return
  ;
