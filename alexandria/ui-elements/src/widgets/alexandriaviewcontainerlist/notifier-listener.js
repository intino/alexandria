var AlexandriaViewContainerListBehaviors = AlexandriaViewContainerListBehaviors || {};

AlexandriaViewContainerListBehaviors.NotifierListener = {

	properties : {
		_listeningToDisplay : { type: Boolean, value: function() { return false; } }
	},

    listenToDisplay : function() {
		if (this.display == null || this._listeningToDisplay) return;
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
        this.when("resizeItem").toSelf().execute(function(parameters) {
        	widget._resizeItem(parameters.value);
        });
        this.when("refreshItemValidation").toSelf().execute(function(parameters) {
        	widget._refreshItemValidation(parameters.value);
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
        this._listeningToDisplay = true;
    }
};