var AlexandriaTabLayoutBehaviors = AlexandriaTabLayoutBehaviors || {};

AlexandriaTabLayoutBehaviors.NotifierListener = {

    listenToDisplay : function() {
        var widget = this;
        this.when("info").toSelf().execute(function(parameters) {
        	widget._info(parameters.value);
        });
        this.when("refreshSelected").toSelf().execute(function(parameters) {
        	widget._refreshSelected(parameters.value);
        });
        this.when("refreshItemList").toSelf().execute(function(parameters) {
        	widget._refreshItemList(parameters.value);
        });
        this.when("user").toSelf().execute(function(parameters) {
        	widget._user(parameters.value);
        });
        this.when("userLoggedOut").toSelf().execute(function(parameters) {
        	widget._userLoggedOut(parameters.value);
        });
        this.when("loading").execute(function(parameters) {
        	widget._loading();
        });
        this.when("loaded").execute(function(parameters) {
        	widget._loaded();
        });
    }
};