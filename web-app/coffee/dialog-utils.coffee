window.showUrlInDialog = (url) ->
  tag = $("<pre class='prettyprint'></pre>") #This tag will the hold the dialog content.
  $.ajax
    url: url
    type: "GET"
    success: (data, textStatus, jqXHR) ->
      console.log(data);
      if typeof data is "object" and data.html #response is assumed to be JSON
        options = data.options;
        console.log(options);
        tag.text(data.html).dialog(
          title : options.title
          modal : options.modal
          height : options.height
          width : options.width
        )
  prettyPrint();
  return;


