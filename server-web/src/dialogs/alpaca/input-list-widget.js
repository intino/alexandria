var AlpacaInputListWidget = function() {
    function widget() {}

    widget.prototype = {};
    widget.prototype.template = function() { return '<div class="inputs"></div>'; };

    widget.prototype.render = function(inputList, container, view) {
        container.innerHTML = this.template();

        if (view != null)
            $(container).addClass(view);

        _renderInputs(inputList, container.querySelector(".inputs"), view);

        window.setTimeout(function() { _focusFirstInput(inputList, container.querySelector(".inputs")); }, 550);
    };

    widget.prototype.refresh = function(inputList, container, view) {
        _refreshInputList(inputList, container, view);
    };

    widget.prototype.getInput = function(key) {
        return $("#" + _idFor(key)).get(0);
    };

    function _renderInputs(inputList, container, view) {
        for (var i=0; i<inputList.length; i++) {
            var input = inputList[i];
            var widget = AlpacaWidgetFactory.get(input.type, input.multiple);
            var inputContainer = _createElementIn(container);
            inputContainer.id = _idFor(input.label);
            widget.render(input, inputContainer, view);
        }
    }

    function _refreshInputList(inputList, container, view) {
        for (var i=0; i<inputList.length; i++) {
            var input = inputList[i];
            var widget = AlpacaWidgetFactory.get(input.type, input.multiple);
            widget.refresh(input, container.querySelector("#" + _idFor(input.label)), view);
        }
    }

    function _focusFirstInput(inputList, container) {
        if (inputList.length == 0)
            return;

        var input = inputList[0];
        var widget = AlpacaWidgetFactory.get(input.type, input.multiple);
        widget.focus(input, container.querySelector("#" + _idFor(input.label)));
    }

    function _createElementIn(container, content) {
        var element = document.createElement("div");

        if (content != null)
            element.innerHTML = content;

        container.appendChild(element);
        return element;
    }

    function _idFor(key) {
        return key.replace(/[^\w]/gi, "");
    }

    return widget;
}();