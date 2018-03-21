var AlpacaInputWidget = function() {
    function widget() {}

    widget.prototype = {};
    widget.prototype.template = function() { return '<div class="component"></div>'; };

    widget.prototype.render = function(input, container, view) {
        container.innerHTML = this.template();
        container.input = input;

        $(container).addClass("input " + input.type + " view" + (view != null ? '_' + view : ''));
        _renderComponent(this, input, container, view);
    };

    widget.prototype.refresh = function(input, container, view) {
        _renderComponent(this, input, container, view);
    };

    widget.prototype.setMessage = function(input, container, status, message) {
        if (input.validator == null)
            return;

        input.validator.validate({ status: status, message: message });
    };

    widget.prototype.focus = function(input, container) {
        var alpacaInput = $(container.querySelector(".component")).alpaca("get");
        return alpacaInput.focus();
    };

    widget.prototype.validate = function(input, container) {
//            if (input.required && (input.data == null || input.data == ""))
//                input.validator.validate({ status: false, message: DialogAlpacaDictionary.labelFor("inputRequired") });
    };

    widget.prototype.isValid = function(input, container) {
        var alpacaInput = $(container.querySelector(".component")).alpaca("get");
        return alpacaInput.isValid();
    };

    widget.prototype.converter = function() {
        return null;
    };

    widget.prototype.addEvents = function(input, alpacaInput, container) {
    };

    function _renderComponent(widget, input, container, view) {
        var converter = widget.converter();
        if (converter == null) return;
        var component = container.querySelector(".component");
        component.innerHTML = "";
        $(component).alpaca(converter.convert(input, view));
        widget.addEvents(input, $(component).alpaca("get"), container);
    }

    return widget;
}();

var AlpacaInputConverter = function() {
    function converter() {}

    converter.prototype = {
        convert : function(input, viewMode) {
            var result = {};

            result.data = this.data(input);
            result.schema = this.schema(input);
            result.options = this.options(input);

            var view = this.view(input, viewMode);
            if (view != null)
                result.view = view;

            return result;
        },

        data: function (input) {
            return input.value ? this._valueOf(input) : input.defaultValue;
        },

        _valueOf : function(input) {
            if (input.multiple == null) return input.value;
            var result = [];
            for (var i=0;i<input.value.length; i++) {
                result.push(input.value[i]);
            }
            return result;
        },

        schema: function (input) {
            var result = {};
            result.title = input.label;
            result.readonly = input.readonly != null ? input.readonly : false;
            result.required = input.required != null ? input.required : false;
            result.inputType = input.type;
            return result;
        },

        options: function (input) {
            var result = {};
            result.name = input.name;
            result.helper = input.helper ? input.helper : false;
            result.placeholder = input.placeholder ? input.placeholder : false;
            result.disabled = input.readonly != null ? input.readonly : false;
            result.hidden = input.visible != null ? !input.visible : false;
            result.events = _addEvents(result, input);
            result.validator = _receiveValidationAlgorithmAndRegisterItToInput.bind(input);
            return result;
        },

        view: function(input, viewMode) {
            var view = null;

            if (_isReadonly(input, viewMode))
                view = 'bootstrap-display';

            return view;
        },

        arrayOf : function(list) {
            var result = [];
            if (list == null) return result;
            for (var i = 0; i < list.length; i++)
                result.push(list[i]);
            return result;
        }
    };

    function _isReadonly(input, view) {
        return (input.readonly != null && input.readonly) || (view != null && view.indexOf("readonly") != -1);
    }

    function _addEvents(alpacaInput, input) {
        return {
            change : function() {
                document.onInputChange(document.pathOf(this), this.getValue());
            }
        }
    }

    function _receiveValidationAlgorithmAndRegisterItToInput(callback) {
        if (this.validator != null) return;

        this.validator = {
            validate : function(validation) {
                callback(validation);
            }
        }
    }

    return converter;
}();
