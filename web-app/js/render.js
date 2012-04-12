(function () {
    var $;
    $ = jQuery;
    window.updateRenderedItem = function (data) {
        var disableElementId, hiddenElementId, name, outcomeValuesText, value, visibleElementId, _i, _j, _k, _len, _len2, _len3, _ref, _ref2, _ref3, _ref4;
        if (data.redirectUrl) {
            $('html').load(data.redirectUrl, data.params, function () {
                return $.getScript("http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML");
            });
            MathJax.Hub.Reprocess();
            console.log('done redirect');
            return;
        } else {
            if (data.responseValues) {
                _ref = data.responseValues;
                for (name in _ref) {
                    value = _ref[name];
                    $('form').field(name, value);
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
    $.fn.field = function (inputName, value) {
        var $inputElement;
        if (typeof inputName !== "string") {
            return false;
        }
        $inputElement = $(this).find("[name=" + inputName + "]");
        switch ($inputElement.attr("type")) {
            case "checkbox":
                $inputElement.each(function (i) {
                    var checked;
                    checked = $.inArray($(this).val(), value.split(",")) >= 0;
                    return $(this).attr('checked', checked);
                });
                break;
            case "radio":
                $inputElement.each(function (i) {
                    if ($(this).val() === value) {
                        return $(this).attr('checked', true);
                    }
                });
                break;
            case "button":
                break;
            case "submit":
                break;
            case undefined:
                switch ($inputElement.attr("id")) {
                    case "select":
                        $inputElement.children('option').each(function (i) {
                            if ($(this).val() === value) {
                                return $(this).attr('selected', 'selected');
                            }
                        });
                        break;
                    default:
                        $(this).append("<input type=\"hidden\" name=\"" + inputName + "\" value=\"" + value + "\" />");
                }
                break;
            default:
                console.log("warning unhandled element " + $inputElement);
                $inputElement.val(value);
        }
        return $inputElement;
    };
}).call(this);
