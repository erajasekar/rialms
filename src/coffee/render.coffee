window.updateRenderedItem = (data) ->
  $(visibleElementId).show() for visibleElementId in data.visibleElementIds
  ;
  $(hiddenElementId).hide() for hiddenElementId in data.hiddenElementIds
  ;
  outcomeValuesText = JSON.stringify(data.outcomeValues)
  ;
  $('#itemOutcomeValues').text(outcomeValuesText)
  ;
  if data.disableElementIds
    $(disableElementId).attr("disabled", true) for disableElementId in data.disableElementIds
  return
  ;
