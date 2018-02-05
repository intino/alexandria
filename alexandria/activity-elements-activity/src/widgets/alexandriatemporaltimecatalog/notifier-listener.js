var AlexandriaTemporalTimeCatalogBehaviors = AlexandriaTemporalTimeCatalogBehaviors || {};

AlexandriaTemporalTimeCatalogBehaviors.NotifierListener = {

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
        this.when("showDialog").toSelf().execute(function(parameters) {
        	widget._showDialog();
        });
        this.when("showTimeNavigator").toSelf().execute(function(parameters) {
        	widget._showTimeNavigator();
        });
        this.when("hideTimeNavigator").toSelf().execute(function(parameters) {
        	widget._hideTimeNavigator();
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
        this.when("loadTimezoneOffset").toSelf().execute(function(parameters) {
        	widget._loadTimezoneOffset();
        });
        this._listeningToDisplay = true;
    }
};