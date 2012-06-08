angular.module("RialmsAngularApp", []).filter "searchByStatus", ->
  (input, filterStatus) ->
    console.log(input + ' == ' + filterStatus)
    if (input.isHeader == filterStatus)
      out = input
    return out
window.TestStatusController = ($scope)->
  self = this
  $scope.getStatusEntries = (testStatusModel) ->
    return testStatusModel

  self.getStyleClass = (statusEntry) ->
    if statusEntry.isSectionTitle
      "nav-header"
    else "active" if statusEntry.isCurrent
  return
