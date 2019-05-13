var AlpacaRadioBoxInputWidget = function() {
	function widget() {}

	widget.prototype = new AlpacaOptionBoxInputWidget();
	widget.prototype.converter = function() {
		return new AlpacaRadioBoxInputConverter();
	};

	return widget;
}();

var AlpacaRadioBoxInputConverter = function() {

	function converter() {}
	converter.prototype = new AlpacaOptionBoxInputConverter();

	converter.prototype.convert = function(input, view) {
		var result = AlpacaOptionBoxInputConverter.prototype.convert.call(this, input, view);
		result.postRender = function (control) {
			_addPostRenderLogic(input, control);
		};
		return result;
	};

	converter.prototype.schema = function(input) {
		var result = AlpacaOptionBoxInputConverter.prototype.schema.call(this, input);
		result.type = "string";
		return result;
	};

	converter.prototype.options = function (input) {
		var result = AlpacaOptionBoxInputConverter.prototype.options.call(this, input);
		result.type = "radio";
		result.emptySelectFirst = true;
		return result;
	};

	function _addPostRenderLogic(input, control) {
		var args = 'control,input,callback';
		var body = 'control.on("change", function() { callback(this, input, this.getValue()); });';
		var f = new Function(args, body);
		return (f(control, input, _atInputChange));
	}

	function _atInputChange(alpacaInput, input, value) {
		document.onInputChange(document.pathOf(alpacaInput), value);
	}

	return converter;
}();
