var AlpacaPictureInputWidget = function() {
	function widget() {}

	widget.prototype = new AlpacaFileInputWidget();
	widget.prototype.converter = function() {
		return new AlpacaPictureInputConverter();
	};

	return widget;
}();

var AlpacaPictureInputConverter = function() {

	function converter() {}
	converter.prototype = new AlpacaFileInputConverter();

	converter.prototype.options = function (input) {
		var result = AlpacaFileInputConverter.prototype.options.call(this, input);
		result.selectionHandler = function(files, data) {
			var img = $(".imagePreview").html("").append("<img style='max-width: 200px; max-height: 200px' src='" + data[0] + "'>");
			var p = $(".imageProperties").html("").append("<p></p>");
			$(p).append("Name: " + files[0].name + "<br/>");
			$(p).append("Size: " + files[0].size + "<br/>");
			$(p).append("Type: " + files[0].type + "<br/>");
			$("#imageInfo").css({
				"display": "block"
			});
		};
		return result;
	};

	return converter;
}();