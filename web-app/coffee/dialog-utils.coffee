window.showUrlInDialog = (url) ->
  tag =  $("#dialogHolder");
  if (!tag.length)
    tag = $("<pre id='dialogHolder' class='prettyprint language-xml'></pre>") #This tag will the hold the dialog content.
  $.ajax
    url: url
    type: "GET"
    success: (data, textStatus, jqXHR) ->
      console.log(data);
      if typeof data is "object" and data.content #response is assumed to be JSON
        options = data.options;
        console.log(options);
        closeButtonLabel = options.closeButtonLabel;
        if closeButtonLabel
          options['buttons'] = {}
          options['buttons'][closeButtonLabel] =
            text: closeButtonLabel
         #   class: "ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only"
            click: ->
              $(this).dialog "close"
        console.log(options);
        tag.html(data.content).dialog(
          options
        )
      prettyPrint();
  return;


