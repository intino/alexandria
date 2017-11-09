var AlpacaDateInputWidget = function() {
	function widget() {}

	widget.prototype = new AlpacaInputWidget();
	widget.prototype.converter = function() {
		return new AlpacaDateInputConverter();
	};
	widget.prototype.addEvents = function(input, alpacaInput, container) {
		alpacaInput.onChange = function(event) {
			document.onInputChange(document.pathOf(alpacaInput), this.getValue());
		}
	};

	return widget;
}();

var AlpacaDateInputConverter = function() {

	function converter() {}
	converter.prototype = new AlpacaInputConverter();

	converter.prototype.schema = function (input) {
		var result = AlpacaInputConverter.prototype.schema.call(this, input);
		result.type = "string";
		result.format = "date";
		return result;
	};

	converter.prototype.options = function (input) {
		var result = AlpacaInputConverter.prototype.options.call(this, input);
		result.type = "date";
		result.dateFormat = input.format ? input.format : "L";
		result.picker = {
			"locale" : document.language
		};
		return result;
	};

	return converter;
}();
