var CatalogListViewWidgetBehaviors = CatalogListViewWidgetBehaviors || {};

CatalogListViewWidgetBehaviors.NotifierListener = {

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
        this.when("refreshSortingList").toSelf().execute(function(parameters) {
        	widget._refreshSortingList(parameters.value);
        });
        this.when("refreshSelectedSorting").toSelf().execute(function(parameters) {
        	widget._refreshSelectedSorting(parameters.value);
        });
        this.when("refreshPicture").toSelf().execute(function(parameters) {
        	widget._refreshPicture(parameters.value);
        });
    }
};