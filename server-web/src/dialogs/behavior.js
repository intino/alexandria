var KonosDialogWidgetBehaviors = KonosDialogWidgetBehaviors || {};

KonosDialogWidgetBehaviors.DialogBehavior = {
    DialogDelegate : "lib/konos-server-web/dialogs/alpaca.html",

    properties : {
        _dialog : Object,
        delegateUrl : String
    },

    _initDelegate : function() {
        this.delegateUrl = this._getUrl() + "/" + this.DialogDelegate + "?random=" + Math.random();
        this.$.frame.onload = this._loadDelegate.bind(this);
    },

    _render : function(dialog) {
        this._dialog = dialog;
        this._loadDelegate();
    },

    _done : function(modification) {
        if (this.delegate == null) return;
        this.delegate.done();
        if (document.onDialogCompleted) document.onDialogCompleted(modification);
    },

    _refresh: function(validation) {
        if (this.delegate == null) return;
        var modifiedInputs = JSON.parse(validation.modifiedInputs);
        this.delegate.sendMessage(validation.input, validation.status, validation.message);
        this.delegate.refresh(modifiedInputs);
        if (modifiedInputs.length > 0)
            this.delegate.focus(validation.input);
    },

    _configuration : function() {
        return {
            language: this.getLanguage(),
            resourceManager: this
        }
    },

    _inputChanged : function(path, value) {
        this.saveValue({ path: path, value: value });
    },

    _inputValueAdded : function(path) {
        this.addValue(path);
    },

    _inputValueRemoved : function(path) {
        this.removeValue(path);
    },

    _operationExecuted : function(operation, parameters) {
        this.execute(operation.name);
    },

    _getUrl: function() {
        var url = this.getProperty("url");
        if (url.charAt(url.length-1) == "/")
            url = url.substring(0, url.length-1);
        return url;
    },

    getProperty: function(name) {
        var widget = this;

        while (widget != null && widget.getAttribute(name) == null)
            widget = widget.parentElement;

        if (widget == null)
            return "";

        return widget.getAttribute(name);
    },

    _loadDelegate : function() {
        if (this._dialog == null) return;

        var frame = this.$.frame;
        var delegate = (frame.contentWindow || frame.contentDocument).document;
        if (!delegate.render) return;

        this.delegate = delegate;
        this.delegate.render(JSON.parse(this._dialog.definition), this._configuration());
        this.delegate.onInputChange = this._inputChanged.bind(this);
        this.delegate.onAddValue = this._inputValueAdded.bind(this);
        this.delegate.onRemoveValue = this._inputValueRemoved.bind(this);
        this.delegate.onOperation = this._operationExecuted.bind(this);
    }
};