var CatalogMapViewWidgetBehaviors = CatalogMapViewWidgetBehaviors || {};

CatalogMapViewWidgetBehaviors.NotifierListener = {

    listenToDisplay : function() {
        var widget = this;
        this.when("refreshView").toSelf().execute(function(parameters) {
        	widget._refreshView(parameters.value);
        });
        this.when("clear").toSelf().execute(function(parameters) {
        	widget._clear();
        });
        this.when("refresh").toSelf().execute(function(parameters) {
        	widget._refresh(parameters.value);
        });
        this.when("refreshPageSize").toSelf().execute(function(parameters) {
        	widget._refreshPageSize(parameters.value);
        });
        this.when("refreshItem").toSelf().execute(function(parameters) {
        	widget._refreshItem(parameters.value);
        });
        this.when("refreshCount").toSelf().execute(function(parameters) {
        	widget._refreshCount(parameters.value);
        });
        this.when("refreshSelection").toSelf().execute(function(parameters) {
        	widget._refreshSelection(parameters.value);
        });
    }
};