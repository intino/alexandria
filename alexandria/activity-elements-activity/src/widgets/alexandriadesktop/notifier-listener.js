var AlexandriaDesktopBehaviors = AlexandriaDesktopBehaviors || {};

AlexandriaDesktopBehaviors.NotifierListener = {

	properties : {
		_listeningToDisplay : { type: Boolean, value: function() { return false; } }
	},

    listenToDisplay : function() {
		if (this.display == null || this._listeningToDisplay) return;
        var widget = this;
        this.when("displayType").toSelf().execute(function(parameters) {
        	widget._displayType(parameters.value);
        });
        this.when("loading").execute(function(parameters) {
        	widget._loading(parameters.value);
        });
        this.when("loaded").execute(function(parameters) {
        	widget._loaded();
        });
        this._listeningToDisplay = true;
    }
};