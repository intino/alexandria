var AlexandriaTabLayoutBehaviors = AlexandriaTabLayoutBehaviors || {};

AlexandriaTabLayoutBehaviors.NotifierListener = {

	properties : {
		_listeningToDisplay : { type: Boolean, value: function() { return false; } }
	},

    listenToDisplay : function() {
		if (this.display == null || this._listeningToDisplay) return;
        var widget = this;
        this.when("info").toSelf().execute(function(parameters) {
        	widget._info(parameters.value);
        });
        this.when("refreshOpened").toSelf().execute(function(parameters) {
        	widget._refreshOpened(parameters.value);
        });
        this.when("refreshItemList").toSelf().execute(function(parameters) {
        	widget._refreshItemList(parameters.value);
        });
        this.when("openDefaultItem").toSelf().execute(function(parameters) {
        	widget._openDefaultItem(parameters.value);
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
        this._listeningToDisplay = true;
    }
};