var AlexandriaContainerViewSetBehaviors = AlexandriaContainerViewSetBehaviors || {};

AlexandriaContainerViewSetBehaviors.NotifierListener = {

	properties : {
		_listeningToDisplay : { type: Boolean, value: function() { return false; } }
	},

    listenToDisplay : function() {
		if (this.display == null || this._listeningToDisplay) return;
        var widget = this;
        this.when("refreshOpened").toSelf().execute(function(parameters) {
        	widget._refreshOpened(parameters.value);
        });
        this.when("refreshItemList").toSelf().execute(function(parameters) {
        	widget._refreshItemList(parameters.value);
        });
        this._listeningToDisplay = true;
    }
};