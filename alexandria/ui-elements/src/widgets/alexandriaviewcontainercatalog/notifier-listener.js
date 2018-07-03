var AlexandriaViewContainerCatalogBehaviors = AlexandriaViewContainerCatalogBehaviors || {};

AlexandriaViewContainerCatalogBehaviors.NotifierListener = {

	properties : {
		_listeningToDisplay : { type: Boolean, value: function() { return false; } }
	},

    listenToDisplay : function() {
		if (this.display == null || this._listeningToDisplay) return;
        var widget = this;
        this.when("displayInfo").toSelf().execute(function(parameters) {
        	widget._displayInfo(parameters.value);
        });
        this._listeningToDisplay = true;
    }
};