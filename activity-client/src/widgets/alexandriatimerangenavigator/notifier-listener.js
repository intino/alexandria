var AlexandriaTimeRangeNavigatorBehaviors = AlexandriaTimeRangeNavigatorBehaviors || {};

AlexandriaTimeRangeNavigatorBehaviors.NotifierListener = {

    listenToDisplay : function() {
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
    }
};