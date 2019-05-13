var AlpacaMemoInputWidget = function() {
    function widget() {}

    widget.prototype = new AlpacaInputWidget();
    widget.prototype.converter = function() {
        return new AlpacaMemoInputConverter();
    };

    return widget;
}();

var AlpacaMemoInputConverter = function() {

    function converter() {}
    converter.prototype = new AlpacaInputConverter();

    converter.prototype.schema = function (input) {
        var result = AlpacaInputConverter.prototype.schema.call(this, input);
        result.type = "string";
        return result;
    };

    converter.prototype.options = function (input) {
        var result = AlpacaInputConverter.prototype.options.call(this, input);
        result.type = input.mode == "raw" ? "textarea" : "ckeditor";
        result.cols = input.height;
        return result;
    };

    return converter;
}();
