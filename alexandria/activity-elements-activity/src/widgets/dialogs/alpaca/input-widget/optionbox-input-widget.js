var AlpacaOptionBoxInputWidget = function() {
	function widget() {}

	widget.prototype = new AlpacaInputWidget();
	widget.prototype.converter = function() {
		return new AlpacaOptionBoxInputConverter();
	};

	return widget;
}();

var AlpacaOptionBoxInputConverter = function() {

	function converter() {}
	converter.prototype = new AlpacaInputConverter();

	converter.prototype.schema = function (input) {
		var result = AlpacaInputConverter.prototype.schema.call(this, input);
		result.enum = this.arrayOf(input.options);
		return result;
	};

	converter.prototype.options = function (input) {
		var result = AlpacaInputConverter.prototype.options.call(this, input);
		result.label = input.label;
		result.optionLabels = this.arrayOf(input.options);
        result.removeDefaultNone = false;
        result.noneLabel = input.placeholder;
		return result;
	};

	return converter;
}();
