var KonosDialogWidgetBehaviors = KonosDialogWidgetBehaviors || {};

KonosDialogWidgetBehaviors.NotifierListener = {

    listenToDisplay : function() {
        var widget = this;
        this.when("render").toSelf().execute(function(parameters) {
        	widget._render(parameters.value);
        });
        this.when("refresh").toSelf().execute(function(parameters) {
        	widget._refresh(parameters.value);
        });
    }
};