var AlpacaCheckBoxInputWidget = function() {
	function widget() {}

	widget.prototype = new AlpacaOptionBoxInputWidget();
	widget.prototype.converter = function() {
		return new AlpacaCheckBoxInputConverter();
	};

	return widget;
}();

var AlpacaCheckBoxInputConverter = function() {

	function converter() {}
	converter.prototype = new AlpacaOptionBoxInputConverter();

	converter.prototype.schema = function(input) {
		return input.value.split("$@");
	};

	converter.prototype.schema = function(input) {
		var result = AlpacaOptionBoxInputConverter.prototype.schema.call(this, input);
		result.type = isBoolean(input) ? "boolean" : "array";
		if (isBoolean(input)) delete(result.enum);
		return result;
	};

	converter.prototype.options = function(input) {
		var result = AlpacaOptionBoxInputConverter.prototype.options.call(this, input);
		result.type = "checkbox";
		return result;
	};

	function isBoolean(input) {
		return input.mode.toLowerCase() == "boolean";
	}

	return converter;
}();
