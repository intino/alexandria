var AlpacaComboBoxInputWidget = function() {
	function widget() {}

	widget.prototype = new AlpacaOptionBoxInputWidget();
	widget.prototype.converter = function() {
		return new AlpacaComboBoxInputConverter();
	};

	return widget;
}();

var AlpacaComboBoxInputConverter = function() {

	function converter() {}
	converter.prototype = new AlpacaOptionBoxInputConverter();

	converter.prototype.schema = function(input) {
		var result = AlpacaOptionBoxInputConverter.prototype.schema.call(this, input);
		result.type = "string";
		return result;
	};

	converter.prototype.options = function (input) {
		var result = AlpacaOptionBoxInputConverter.prototype.options.call(this, input);
		result.type = "select";
		return result;
	};

	return converter;
}();
