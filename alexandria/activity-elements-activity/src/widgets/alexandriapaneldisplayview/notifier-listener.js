var AlexandriaPanelDisplayViewBehaviors = AlexandriaPanelDisplayViewBehaviors || {};

AlexandriaPanelDisplayViewBehaviors.NotifierListener = {

    listenToDisplay : function() {
        var widget = this;
        this.when("displayType").toSelf().execute(function(parameters) {
        	widget._displayType(parameters.value);
        });
    }
};