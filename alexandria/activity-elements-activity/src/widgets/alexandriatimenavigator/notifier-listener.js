var AlexandriaTimeNavigatorBehaviors = AlexandriaTimeNavigatorBehaviors || {};

AlexandriaTimeNavigatorBehaviors.NotifierListener = {

	properties : {
		_listeningToDisplay : { type: Boolean, value: function() { return false; } }
	},

    listenToDisplay : function() {
		if (this.display == null || this._listeningToDisplay) return;
        var widget = this;
        this.when("refreshScales").toSelf().execute(function(parameters) {
        	widget._refreshScales(parameters.value);
        });
        this.when("refreshScale").toSelf().execute(function(parameters) {
        	widget._refreshScale(parameters.value);
        });
        this.when("refreshOlapRange").toSelf().execute(function(parameters) {
        	widget._refreshOlapRange(parameters.value);
        });
        this.when("refreshDate").toSelf().execute(function(parameters) {
        	widget._refreshDate(parameters.value);
        });
        this.when("refreshState").toSelf().execute(function(parameters) {
        	widget._refreshState(parameters.value);
        });
        this._listeningToDisplay = true;
    }
};