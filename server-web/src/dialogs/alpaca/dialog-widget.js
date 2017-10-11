var AlpacaDialogWidget = function() {
    function widget() {}

    widget.prototype = {};
    widget.prototype.template = function() { return '<h3 class="label"></h3><div class="description"></div><div class="done alert-success" role="alert"></div><div class="tabs"></div><div class="toolbar"></div></div>'; };

    widget.prototype.render = function(dialog, container) {
        container.innerHTML = this.template();

        _renderLabel(dialog, container.querySelector(".label"));
        _renderDone(dialog, container.querySelector(".done"));
        _renderDescription(dialog, container.querySelector(".description"));
        _renderTabs(dialog, container.querySelector(".tabs"));
        _renderToolbar(dialog, container);
    };

    widget.prototype.refresh = function(inputList, container, view) {
        var dialogWidget = AlpacaWidgetFactory.get("inputlist");
        dialogWidget.refresh(inputList, container, view);
    };

    widget.prototype.done = function(container) {
        container.querySelector(".done").style.display = "block";
        container.querySelector(".tabs").style.display = "none";
        container.querySelector(".toolbar").style.display = "none";
    };

    widget.prototype.getInput = function(key) {
        return AlpacaWidgetFactory.get("inputlist").getInput(key);
    };

    function _renderLabel(dialog, container) {
        container.innerHTML = dialog.label;
    }

    function _renderDone(dialog, container) {
        container.innerHTML = DialogAlpacaDictionary.labelFor("done");
    }

    function _renderDescription(dialog, container) {
        container.innerHTML = dialog.description;
    }

    function _renderTabs(dialog, container) {
        var tabs = dialog.tabList;
        _createTabs(tabs, container);
        for (var i=0; i<tabs.length; i++) {
            var tab = _tabElementOf(container, i);
            var readonly = dialog.readonly;
            var inputList = tabs[i].inputList;
            inputList.readonly = readonly;
            _renderInputList(inputList, tab, readonly ? "component-readonly" : "component-editable");
        }
    }

    function _createTabs(tabs, container) {
        var result = "";

        if (tabs.length > 1) {
            result += '<ul class="nav nav-tabs" role="tablist">';
            for (var i = 0; i < tabs.length; i++)
                result += '<li role="presentation"' + (i == 0 ? ' class="active"' : '') + '><a href="#tab' + i + '" aria-controls="home" role="tab" data-toggle="tab">' + tabs[i].label + '</a></li>';
            result += '</ul>';
        }

        result += '<div class="tab-content">';
        for (var i=0; i<tabs.length; i++)
            result += '<div role="tabpanel" class="tab-pane fade' + (i==0?" in active":"") + '" id="tab' + i + '"></div>';
        result += "</div>";

        container.innerHTML = result;

        return container;
    }

    function _renderInputList(inputList, container, view) {
        var widget = AlpacaWidgetFactory.get("inputlist");
        widget.render(inputList, container, view);
    }

    function _tabElementOf(tabs, pos) {
        return tabs.querySelector('#tab' + pos);
    }

    function _renderToolbar(dialog, container) {
        if (dialog.readonly)
            return;

        var toolbarContainer = container.querySelector(".toolbar");
        var operations = dialog.toolbar.operationList;

        for (var i=0; i<operations.length; i++) {
            var label = DialogAlpacaDictionary.labelFor(operations[i].name);
            _renderButton({ name: operations[i].name, label: label != null ? label : operations[i].label }, toolbarContainer);
        }
    }

    function _renderButton(button, container, action) {
        var element = _createElementIn(container, '<button type="button" class="btn btn-primary" autocomplete="off">' + button.label + '</button>');
        var defaultAction = function() { document.onOperation(button.name); };
        $(element.querySelector('button')).on('click', action != null ? action : defaultAction);
    }

    function _createElementIn(container, content) {
        var element = document.createElement("div");

        if (content != null)
            element.innerHTML = content;

        container.appendChild(element);
        return element;
    }

    return widget;
}();
