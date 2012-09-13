window.showUrlInDialog = (url) ->
  tag = $("<pre class='prettyprint language-xml'></pre>") #This tag will the hold the dialog content.
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
          options['buttons'][closeButtonLabel] =  ->
              $(this).dialog "close"
        console.log(options);
        tag.html(data.content).dialog(
          options
        )
      prettyPrint();
  return;


