var AlpacaMultipleInputWidget = function() {
    function widget() {}

    widget.prototype = new AlpacaInputWidget();
    widget.prototype.converter = function() {
        return new AlpacaMultipleInputConverter();
    };

    return widget;
}();

var AlpacaMultipleInputConverter = function() {

    function converter() {}
    converter.prototype = new AlpacaInputConverter();

    converter.prototype.schema = function(input) {
        var result = AlpacaInputConverter.prototype.schema.call(this, input);
        result.type = "array";
        result.items = AlpacaWidgetFactory.get(input.type, false).converter().schema(input);

        if (input.multiple.min != -1)
            result.minItems = input.multiple.min;

        if (input.multiple.max != -1)
            result.maxItems = input.multiple.max;

        return result;
    };

    converter.prototype.options = function(input) {
        var result = AlpacaInputConverter.prototype.options.call(this, input);
        var options = AlpacaWidgetFactory.get(input.type, false).converter().options(input);

        if (input.type == "section") {
            result.items = {};
            result.items.fields = options.fields;
        }
        else {
            result.items = options;
            result.items.name = null;
        }

        result.events = null;
        result.hideToolbarWithChildren = false;
        result.toolbarSticky = true;
        result.toolbar = {
            "actions" : [{
                "action": "add",
                "click" : function(key, action) {
                    document.onAddValue(document.pathOf(this) + "." + 0);
                    this.handleToolBarAddItemClick(null);
                }
            }]
        };
        result.actionbar = {
            "actions" : [{
                "action": "add",
                "click" : function(key, action, itemIndex) {
                    document.onAddValue(document.pathOf(this) + "." + itemIndex);
                    this.handleToolBarAddItemClick(null);
                }
            }, {
                "action": "remove",
                "click" : function(key, action, itemIndex) {
                    document.onRemoveValue(document.pathOf(this) + "." + itemIndex);
                    this.removeItem(itemIndex);
                }
            }, {
                "action": "up", enabled: false
            }, {
                "action": "down", enabled: false
            }]
        };
        return result;
    };

    return converter;
}();