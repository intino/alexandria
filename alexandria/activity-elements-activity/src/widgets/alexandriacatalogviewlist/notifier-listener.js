var AlexandriaCatalogViewListBehaviors = AlexandriaCatalogViewListBehaviors || {};

AlexandriaCatalogViewListBehaviors.NotifierListener = {

	properties : {
		_listeningToDisplay : { type: Boolean, value: function() { return false; } }
	},

    listenToDisplay : function() {
		if (this.display == null || this._listeningToDisplay) return;
        var widget = this;
        this.when("refreshTarget").toSelf().execute(function(parameters) {
        	widget._refreshTarget(parameters.value);
        });
        this.when("refreshViewList").toSelf().execute(function(parameters) {
        	widget._refreshViewList(parameters.value);
        });
        this.when("refreshSelectedView").toSelf().execute(function(parameters) {
        	widget._refreshSelectedView(parameters.value);
        });
        this._listeningToDisplay = true;
    }
};