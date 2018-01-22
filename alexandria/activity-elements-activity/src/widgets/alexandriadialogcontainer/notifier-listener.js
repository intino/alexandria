var AlexandriaDialogContainerBehaviors = AlexandriaDialogContainerBehaviors || {};

AlexandriaDialogContainerBehaviors.NotifierListener = {

    listenToDisplay : function() {
		if (this.display == null) return;
        var widget = this;
        this.when("refreshDialog").toSelf().execute(function(parameters) {
        	widget._refreshDialog(parameters.value);
        });
    }
};