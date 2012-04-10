(function () {
    window.updateRenderedItem = function (data) {
        var disableElementId, hiddenElementId, name, outcomeValuesText, value, visibleElementId, _i, _j, _k, _len, _len2, _len3, _ref, _ref2, _ref3, _ref4;
        if (data.redirectUrl) {
            $.post(data.redirectUrl, function (htmlResp) {
                $('html').html(htmlResp);
            });
        } else {
            if (data.responseValues) {
                _ref = data.responseValues;
                for (name in _ref) {
                    value = _ref[name];
                    $("input[name = " + name + "]").val(value);
                }
            }
            if (data.visibleElementIds) {
                _ref2 = data.visibleElementIds;
                for (_i = 0, _len = _ref2.length; _i < _len; _i++) {
                    visibleElementId = _ref2[_i];
                    $(visibleElementId).show();
                }
            }
            if (data.hiddenElementIds) {
                _ref3 = data.hiddenElementIds;
                for (_j = 0, _len2 = _ref3.length; _j < _len2; _j++) {
                    hiddenElementId = _ref3[_j];
                    $(hiddenElementId).hide();
                }
            }
            if (data.outcomeValues) {
                outcomeValuesText = JSON.stringify(data.outcomeValues);
                $('#itemOutcomeValues').text(outcomeValuesText);
            }
            if (data.disableElementIds) {
                _ref4 = data.disableElementIds;
                for (_k = 0, _len3 = _ref4.length; _k < _len3; _k++) {
                    disableElementId = _ref4[_k];
                    $(disableElementId).attr("disabled", true);
                }
            }
        }
    };
}).call(this);
