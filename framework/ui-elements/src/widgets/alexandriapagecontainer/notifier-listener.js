var AlexandriaPageContainerBehaviors = AlexandriaPageContainerBehaviors || {};

AlexandriaPageContainerBehaviors.NotifierListener = {

	properties : {
		_listeningToDisplay : { type: Boolean, value: function() { return false; } }
	},

    listenToDisplay : function() {
		if (this.display == null || this._listeningToDisplay) return;
        var widget = this;
        this.when("refreshLocation").toSelf().execute(function(parameters) {
        	widget._refreshLocation(parameters.value);
        });
        this._listeningToDisplay = true;
    }
};