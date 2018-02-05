var AlexandriaItemBehaviors = AlexandriaItemBehaviors || {};

AlexandriaItemBehaviors.NotifierListener = {

	properties : {
		_listeningToDisplay : { type: Boolean, value: function() { return false; } }
	},

    listenToDisplay : function() {
		if (this.display == null || this._listeningToDisplay) return;
        var widget = this;
        this.when("refresh").toSelf().execute(function(parameters) {
        	widget._refresh(parameters.value);
        });
        this.when("refreshMode").toSelf().execute(function(parameters) {
        	widget._refreshMode(parameters.value);
        });
        this.when("refreshEmptyMessage").toSelf().execute(function(parameters) {
        	widget._refreshEmptyMessage(parameters.value);
        });
        this._listeningToDisplay = true;
    }
};