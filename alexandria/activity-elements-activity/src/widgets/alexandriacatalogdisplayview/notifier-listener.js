var AlexandriaCatalogDisplayViewBehaviors = AlexandriaCatalogDisplayViewBehaviors || {};

AlexandriaCatalogDisplayViewBehaviors.NotifierListener = {

    listenToDisplay : function() {
        var widget = this;
        this.when("refreshView").toSelf().execute(function(parameters) {
        	widget._refreshView(parameters.value);
        });
        this.when("displayType").toSelf().execute(function(parameters) {
        	widget._displayType(parameters.value);
        });
    }
};