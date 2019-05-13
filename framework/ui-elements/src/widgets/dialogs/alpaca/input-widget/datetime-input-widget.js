var AlpacaDateTimeInputWidget = function() {
	function widget() {}

	widget.prototype = new AlpacaDateInputWidget();
	widget.prototype.converter = function() {
		return new AlpacaDateTimeInputConverter();
	};

	return widget;
}();

var AlpacaDateTimeInputConverter = function() {

	function converter() {}
	converter.prototype = new AlpacaInputConverter();

	converter.prototype.schema = function (input) {
		var result = AlpacaInputConverter.prototype.schema.call(this, input);
		result.type = "string";
		result.format = "datetime";
		return result;
	};

	converter.prototype.options = function (input) {
		var result = AlpacaInputConverter.prototype.options.call(this, input);
		result.type = "datetime";
		result.dateFormat = input.format ? input.format : "LLL";
		result.picker = {
			"locale" : document.language
		};
		return result;
	};

	return converter;
}();