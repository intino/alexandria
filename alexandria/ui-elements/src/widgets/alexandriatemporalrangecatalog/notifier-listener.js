var AlexandriaTemporalRangeCatalogBehaviors = AlexandriaTemporalRangeCatalogBehaviors || {};

AlexandriaTemporalRangeCatalogBehaviors.NotifierListener = {

	properties : {
		_listeningToDisplay : { type: Boolean, value: function() { return false; } }
	},

    listenToDisplay : function() {
		if (this.display == null || this._listeningToDisplay) return;
        var widget = this;
        this.when("refreshCatalog").toSelf().execute(function(parameters) {
        	widget._refreshCatalog(parameters.value);
        });
        this.when("refreshFiltered").toSelf().execute(function(parameters) {
        	widget._refreshFiltered(parameters.value);
        });
        this.when("refreshBreadcrumbs").toSelf().execute(function(parameters) {
        	widget._refreshBreadcrumbs(parameters.value);
        });
        this.when("refreshNavigatorLayout").toSelf().execute(function(parameters) {
        	widget._refreshNavigatorLayout(parameters.value);
        });
        this.when("showDialogBox").toSelf().execute(function(parameters) {
        	widget._showDialogBox();
        });
        this.when("showTimeRangeNavigator").toSelf().execute(function(parameters) {
        	widget._showTimeRangeNavigator();
        });
        this.when("hideTimeRangeNavigator").toSelf().execute(function(parameters) {
        	widget._hideTimeRangeNavigator();
        });
        this.when("createPanel").toSelf().execute(function(parameters) {
        	widget._createPanel(parameters.value);
        });
        this.when("showPanel").toSelf().execute(function(parameters) {
        	widget._showPanel();
        });
        this.when("hidePanel").toSelf().execute(function(parameters) {
        	widget._hidePanel();
        });
        this.when("notifyUser").toSelf().execute(function(parameters) {
        	widget._notifyUser(parameters.value);
        });
        this.when("itemsArrival").toSelf().execute(function(parameters) {
        	widget._itemsArrival();
        });
        this.when("loadTimezoneOffset").toSelf().execute(function(parameters) {
        	widget._loadTimezoneOffset();
        });
        this._listeningToDisplay = true;
    }
};