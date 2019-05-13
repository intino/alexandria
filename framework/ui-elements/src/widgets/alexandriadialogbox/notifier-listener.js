var AlexandriaDialogBoxBehaviors = AlexandriaDialogBoxBehaviors || {};

AlexandriaDialogBoxBehaviors.NotifierListener = {

	properties : {
		_listeningToDisplay : { type: Boolean, value: function() { return false; } }
	},

    listenToDisplay : function() {
		if (this.display == null || this._listeningToDisplay) return;
        var widget = this;
        this.when("refreshDisplay").toSelf().execute(function(parameters) {
        	widget._refreshDisplay(parameters.value);
        });
        this.when("refreshSettings").toSelf().execute(function(parameters) {
        	widget._refreshSettings(parameters.value);
        });
        this.when("closeDialog").toSelf().execute(function(parameters) {
        	widget._closeDialog();
        });
        this._listeningToDisplay = true;
    }
};