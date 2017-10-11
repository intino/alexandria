var AlpacaWidgetFactory = function() {
	var factory = {};
	factory.widgets = null;

	factory.get = function(type, multiple) {
		register(factory);
		if (multiple != null && multiple !== false) return this.widgets["multiple"];
		return this.widgets[type.toLowerCase()];
	};

	function register(factory) {
		if (factory.widget != null)
			return;

		factory.widgets = {};
		factory.widgets["dialog"] = new AlpacaDialogWidget();
		factory.widgets["inputlist"] = new AlpacaInputListWidget();
		factory.widgets["text"] = new AlpacaTextInputWidget();
		factory.widgets["memo"] = new AlpacaMemoInputWidget();
		factory.widgets["password"] = new AlpacaPasswordInputWidget();
		factory.widgets["radiobox"] = new AlpacaRadioBoxInputWidget();
		factory.widgets["checkbox"] = new AlpacaCheckBoxInputWidget();
		factory.widgets["combobox"] = new AlpacaComboBoxInputWidget();
		factory.widgets["file"] = new AlpacaFileInputWidget();
		factory.widgets["picture"] = new AlpacaPictureInputWidget();
		factory.widgets["document"] = new AlpacaDocumentInputWidget();
		factory.widgets["date"] = new AlpacaDateInputWidget();
		factory.widgets["datetime"] = new AlpacaDateTimeInputWidget();
		factory.widgets["section"] = new AlpacaSectionInputWidget();
		factory.widgets["multiple"] = new AlpacaMultipleInputWidget();
	}

	return factory;
}();
