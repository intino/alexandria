var AlexandriaTemporalRangeCatalogBehaviors = AlexandriaTemporalRangeCatalogBehaviors || {};

AlexandriaTemporalRangeCatalogBehaviors.NotifierListener = {

    listenToDisplay : function() {
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
        this.when("loadTimezoneOffset").toSelf().execute(function(parameters) {
        	widget._loadTimezoneOffset();
        });
    }
};