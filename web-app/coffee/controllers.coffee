window.TestStatusController = ($scope)->



  $scope.getStatusEntries = () ->
    statusEntries = []
    angular.forEach($scope.testStatusModel, (value,key)->
      statusEntries.push
        identifier: key
      return
    )
    console.log('statusEntries ' + statusEntries)
    statusEntries;

  return
