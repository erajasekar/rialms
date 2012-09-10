showUrlInDialog = (url, options) ->
  options = options or {}
  tag = $("<div></div>") #This tag will the hold the dialog content.
  $.ajax
    url: url
    type: (options.type or "GET")
    beforeSend: options.beforeSend
    error: options.error
    complete: options.complete
    success: (data, textStatus, jqXHR) ->
      if typeof data is "object" and data.html #response is assumed to be JSON
        tag.html(data.html).dialog(
          modal: options.modal
          title: data.title
        ).dialog "open"
      else #response is assumed to be HTML
        tag.html(data).dialog(
          modal: options.modal
          title: options.title
        ).dialog "open"
