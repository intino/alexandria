var AlexandriaDialogContainerBehaviors = AlexandriaDialogContainerBehaviors || {};

AlexandriaDialogContainerBehaviors.NotifierListener = {

	properties : {
		_listeningToDisplay : { type: Boolean, value: function() { return false; } }
	},

    listenToDisplay : function() {
		if (this.display == null || this._listeningToDisplay) return;
        var widget = this;
        this.when("refreshDialog").toSelf().execute(function(parameters) {
        	widget._refreshDialog(parameters.value);
        });
        this._listeningToDisplay = true;
    }
};