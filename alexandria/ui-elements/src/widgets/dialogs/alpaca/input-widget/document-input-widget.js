const FileInputName = "file";

var AlpacaDocumentInputWidget = function() {
	function widget() {}

	widget.prototype = new AlpacaInputWidget();
	widget.prototype.template = function() { return '<div class="title"></div><div class="component"></div>'; };
	widget.prototype.render = function(input, container, view) {
		AlpacaInputWidget.prototype.render.call(this, input, container, view);
		_renderTitle(input, container, view);
	};
	widget.prototype.converter = function() {
		return new AlpacaDocumentInputConverter();
	};

	function _renderTitle(input, container, view) {
		container.querySelector(".title").innerHTML = input.label;
	}

	return widget;
}();

var AlpacaDocumentInputConverter = function() {
	function converter() {}
	converter.prototype = new AlpacaInputConverter();
	converter.template = '<div class="wizard"><div data-dialogs.alpaca-wizard-role="step" data-dialogs.alpaca-wizard-step-title="::editDocumentStep1Title::" data-dialogs.alpaca-wizard-step-description="::editDocumentStep1Subtitle::"><div class="step-description">::editDocumentStep1Description::</div><button type="button" class="btn btn-primary editDocumentDownloadButton" autocomplete="off">::editDocumentDownload::</button></div><div data-dialogs.alpaca-wizard-role="step" data-dialogs.alpaca-wizard-step-title="::editDocumentStep2Title::" data-dialogs.alpaca-wizard-step-description="::editDocumentStep2Subtitle::"><div class="step-description">::editDocumentStep2Description::</div><button type="button" class="btn btn-primary editDocumentModifiedButton" autocomplete="off">::editDocumentModified::</button></div><div data-dialogs.alpaca-wizard-role="step" data-dialogs.alpaca-wizard-step-title="::editDocumentStep3Title::" data-dialogs.alpaca-wizard-step-description="::editDocumentStep3Subtitle::"><div class="step-description">::editDocumentStep3Description::</div><div data-dialogs.alpaca-layout-binding="file"></div></div></div>';

	converter.prototype.convert = function(input, view) {
		var result = AlpacaInputConverter.prototype.convert.call(this, input, view);
		result.postRender = function (control) {
			_addPostRenderLogic(input, control);
		};
		return result;
	};

	converter.prototype.schema = function(input) {
		var result = AlpacaInputConverter.prototype.schema.call(this, input);
		result.type = "object";
		result.properties = {};
		result.properties[FileInputName] = { title: "", type: "string", format: "uri" };
		return result;
	};

	converter.prototype.options = function(input) {
		var result = AlpacaInputConverter.prototype.options.call(this, input);
		result.inputs = {};
		result.inputs[FileInputName] = { type: "file", styled: true };
		result.events = _addEvents(result, input);
		return result;
	};

	converter.prototype.view = function(input, viewMode) {
		var view = {};
		view.parent = "bootstrap-edit-horizontal";
		view.layout = { template: _template() };
		view.wizard = {
			hideSubmitButton: true,
			showProgressBar: false
		};
		return view;
	};

	function _template() {
		var template = converter.template;
		var words = DialogAlpacaDictionary.words();
		for (var key in words)
			template = template.replace("::" + key + "::", words[key]);
		return template;
	}

	function _addPostRenderLogic(input, control) {
		var args = 'control,input,downloadCallback,finishCallback';
		var body = '$(".editDocumentDownloadButton").on("click", function() { downloadCallback(input); control.trigger("moveToStep", { "index": 1 }); });$(".editDocumentModifiedButton").on("click", function() { control.trigger("moveToStep", { "index": 2 }); });control.childrenByPropertyId["' + FileInputName + '"].on("change", finishCallback.bind(control));';
		var f = new Function(args, body);
		return (f(control, input, _downloadCallback, _finishCallback));
	}

	function _downloadCallback(input) {
		if (input.value != null)
			window.location.href = document.resourceManager.downloadResource(input.name);
	}

	function _finishCallback(control) {
		control.trigger("advanceOrSubmit");
	}

	function _addEvents(alpacaInput, input) {
		return {
			change : function(event) {
				AlpacaFileInputWidget.upload(input, event.target.files);
			}
		}
	}

	return converter;
}();
