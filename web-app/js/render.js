(function() {
  window.updateRenderedItem = function(data) {
    var disableElementId, hiddenElementId, outcomeValuesText, visibleElementId, _i, _j, _k, _len, _len2, _len3, _ref, _ref2, _ref3;
    if (data.visibleElementIds) {
      _ref = data.visibleElementIds;
      for (_i = 0, _len = _ref.length; _i < _len; _i++) {
        visibleElementId = _ref[_i];
        $(visibleElementId).show();
      }
    }
    if (data.hiddenElementIds) {
      _ref2 = data.hiddenElementIds;
      for (_j = 0, _len2 = _ref2.length; _j < _len2; _j++) {
        hiddenElementId = _ref2[_j];
        $(hiddenElementId).hide();
      }
    }
    if (data.outcomeValues) {
      outcomeValuesText = JSON.stringify(data.outcomeValues);
      $('#itemOutcomeValues').text(outcomeValuesText);
    }
    if (data.disableElementIds) {
      _ref3 = data.disableElementIds;
      for (_k = 0, _len3 = _ref3.length; _k < _len3; _k++) {
        disableElementId = _ref3[_k];
        $(disableElementId).attr("disabled", true);
      }
    }
  };
}).call(this);
