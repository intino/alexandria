var AlexandriaPanelBehaviors = AlexandriaPanelBehaviors || {};

AlexandriaPanelBehaviors.NotifierListener = {

    listenToDisplay : function() {
        var widget = this;
        this.when("refreshTarget").toSelf().execute(function(parameters) {
        	widget._refreshTarget(parameters.value);
        });
        this.when("refreshViewList").toSelf().execute(function(parameters) {
        	widget._refreshViewList(parameters.value);
        });
        this.when("refreshBreadcrumbs").toSelf().execute(function(parameters) {
        	widget._refreshBreadcrumbs(parameters.value);
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
        this.when("showDialog").toSelf().execute(function(parameters) {
        	widget._showDialog();
        });
    }
};