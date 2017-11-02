var DialogContainerWidgetBehaviors = DialogContainerWidgetBehaviors || {};

DialogContainerWidgetBehaviors.NotifierListener = {

    listenToDisplay : function() {
        var widget = this;
        this.when("refreshDialog").toSelf().execute(function(parameters) {
        	widget._refreshDialog(parameters.value);
        });
    }
};