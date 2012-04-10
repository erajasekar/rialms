window.updateRenderedItem = (data) ->
  if data.redirectUrl
    $('html').load(data.redirectUrl,data.params, ->
        $.getScript("http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML")
        alert('done')
    )
    return
  else
    if data.responseValues
      for name,value of data.responseValues
        $("input[name = " + name + "]:not(:button)").val(value)
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
