var AlpacaSectionInputWidget = function() {
	function widget() {}

	widget.prototype = new AlpacaInputWidget();
	widget.prototype.converter = function() {
		return new AlpacaSectionInputConverter();
	};

	return widget;
}();

var AlpacaSectionInputConverter = function() {

	function converter() {}
	converter.prototype = new AlpacaInputConverter();

	converter.prototype.schema = function(input) {
		var result = AlpacaInputConverter.prototype.schema.call(this, input);
		result.type = "object";
		result.section = true;
		result.properties = {};

		for (var i=0; i<input.inputList.length; i++) {
			var child = input.inputList[i];
			var widget = AlpacaWidgetFactory.get(child.type, child.multiple);
			result.properties[child.label] = widget.converter().schema(child);
		}

		return result;
	};

	converter.prototype.options = function(input) {
		var result = AlpacaInputConverter.prototype.options.call(this, input);
		result.fields = {};
		result.events = null;

		for (var i=0; i<input.inputList.length; i++) {
			var child = input.inputList[i];
			var widget = AlpacaWidgetFactory.get(child.type, child.multiple);
			result.fields[child.label] = widget.converter().options(child);
		}

		return result;
	};

	return converter;
}();
