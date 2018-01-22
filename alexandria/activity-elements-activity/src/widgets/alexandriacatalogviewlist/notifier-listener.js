var AlexandriaCatalogViewListBehaviors = AlexandriaCatalogViewListBehaviors || {};

AlexandriaCatalogViewListBehaviors.NotifierListener = {

    listenToDisplay : function() {
		if (this.display == null) return;
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
    }
};