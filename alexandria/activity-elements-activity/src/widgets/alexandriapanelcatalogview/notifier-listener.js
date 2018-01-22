var AlexandriaPanelCatalogViewBehaviors = AlexandriaPanelCatalogViewBehaviors || {};

AlexandriaPanelCatalogViewBehaviors.NotifierListener = {

    listenToDisplay : function() {
		if (this.display == null) return;
        var widget = this;
        this.when("displayType").toSelf().execute(function(parameters) {
        	widget._displayType(parameters.value);
        });
    }
};