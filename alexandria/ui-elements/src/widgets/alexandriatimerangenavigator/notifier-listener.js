var AlexandriaTimeRangeNavigatorBehaviors = AlexandriaTimeRangeNavigatorBehaviors || {};

AlexandriaTimeRangeNavigatorBehaviors.NotifierListener = {

	properties : {
		_listeningToDisplay : { type: Boolean, value: function() { return false; } }
	},

    listenToDisplay : function() {
		if (this.display == null || this._listeningToDisplay) return;
        var widget = this;
        this.when("refreshScales").toSelf().execute(function(parameters) {
        	widget._refreshScales(parameters.value);
        });
        this.when("refreshOlapRange").toSelf().execute(function(parameters) {
        	widget._refreshOlapRange(parameters.value);
        });
        this.when("refreshZoomRange").toSelf().execute(function(parameters) {
        	widget._refreshZoomRange(parameters.value);
        });
        this.when("refreshRange").toSelf().execute(function(parameters) {
        	widget._refreshRange(parameters.value);
        });
        this._listeningToDisplay = true;
    }
};