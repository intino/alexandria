var AlexandriaEditorBehaviors = AlexandriaEditorBehaviors || {};

AlexandriaEditorBehaviors.NotifierListener = {

	properties : {
		_listeningToDisplay : { type: Boolean, value: function() { return false; } }
	},

    listenToDisplay : function() {
		if (this.display == null || this._listeningToDisplay) return;
        var widget = this;
        this.when("displayType").toSelf().execute(function(parameters) {
        	widget._displayType(parameters.value);
        });
        this.when("saved").toSelf().execute(function(parameters) {
        	widget._saved();
        });
        this._listeningToDisplay = true;
    }
};