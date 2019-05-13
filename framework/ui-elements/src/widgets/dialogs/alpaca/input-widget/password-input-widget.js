var AlpacaPasswordInputWidget = function() {
	function widget() {}

	widget.prototype = new AlpacaInputWidget();
	widget.prototype.converter = function() {
		return new AlpacaPasswordInputConverter();
	};

	return widget;
}();

var AlpacaPasswordInputConverter = function() {

	function converter() {}
	converter.prototype = new AlpacaInputConverter();

	converter.prototype.schema = function(input) {
		var result = AlpacaInputConverter.prototype.schema.call(this, input);
		result.type = "string";

		if (existsLength(input)) {
			var length = input.validation.length;
			result.maxLength = length.min;
			result.minLenght = length.max;
		}

		return result;
	};

	converter.prototype.options = function(input) {
		var result = AlpacaInputConverter.prototype.options.call(this, input);
		result.type = "password";
		if (existsLength(input)) {
			result.size = input.validation.length.max;
			result.constrainMaxLength = true;
			result.constrainMinLength = true;
			result.showMaxLengthIndicator = true;
		}
		return result;
	};

	function existsLength(input) {
		return input.validation != null && input.validation.length != null;
	}

	return converter;
}();
