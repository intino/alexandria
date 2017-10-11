var AlpacaTextInputWidget = function() {
	function widget() {}

	widget.prototype = new AlpacaInputWidget();
	widget.prototype.converter = function() {
		return new AlpacaTextInputConverter();
	};

	return widget;
}();

var AlpacaTextInputConverter = function() {

	function converter() {}
	converter.prototype = new AlpacaInputConverter();

	converter.prototype.schema = function(input) {
		var result = AlpacaInputConverter.prototype.schema.call(this, input);
		result.type = "string";

		if (allowedValues(input)) result.enum = this.arrayOf(input.validation.allowedValues);
		if (disallowedValues(input)) result.disallow = this.arrayOf(input.validation.disallowedValues);

		if (existsLength(input)) {
			var length = input.validation.length;
			result.maxLength = length.min;
			result.minLenght = length.max;
		}

		return result;
	};

	converter.prototype.options = function(input) {
		var result = AlpacaInputConverter.prototype.options.call(this, input);
		result.type = input.edition ? input.edition : "text";
		if (disallowEmptySpaces(input)) result.disallowEmptySpaces = input.disallowEmptySpaces;
		if (existsMask(input)) result.maskString = input.validation.mask;
		if (existsLength(input)) {
			result.size = input.validation.length.max;
			result.constrainMaxLength = true;
			result.constrainMinLength = true;
			result.showMaxLengthIndicator = true;
		}
		addTypeAhead(result, input);
		return result;
	};

	function allowedValues(input) {
		return input.validation != null && input.validation.allowedValues != null && input.validation.allowedValues.length > 0;
	}

	function disallowedValues(input) {
		return input.validation != null && input.validation.allowedValues != null && input.validation.disallowedValues.length > 0;
	}

	function disallowEmptySpaces(input) {
		return input.validation != null && input.validation.disallowEmptySpaces;
	}

	function existsLength(input) {
		return input.validation != null && input.validation.length != null;
	}

	function existsMask(input) {
		return input.validation != null && input.validation.mask != null;
	}

	function addTypeAhead(result, input) {
		if (input.typeAhead == null)
			return;

		var typeAhead = input.typeAhead;
		result.typeahead = {
			config : {
				autoselect: typeAhead.autoselect,
				highlight: typeAhead.highlight,
				hint: typeAhead.hint,
				minLength: typeAhead.minLength
			},
			source : {
				type: "local",
				source: function(query) {
					var result = [];
					var values = document.source.load(query);
					for(var i=0; i<values.length; i++) result.push({ value: values[i] });
					return result;
				}
			}
		};
	}

	return converter;
}();
