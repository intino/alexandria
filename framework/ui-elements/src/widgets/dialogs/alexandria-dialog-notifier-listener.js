var AlexandriaDialogBehaviors = AlexandriaDialogBehaviors || {};

AlexandriaDialogBehaviors.NotifierListener = {

    listenToDisplay : function() {
        var widget = this;
        this.when("render").toSelf().execute(function(parameters) {
        	widget._render(parameters.value);
        });
        this.when("notifyUser").toSelf().execute(function(parameters) {
            widget._notifyUser(parameters.value);
        });
        this.when("refresh").toSelf().execute(function(parameters) {
        	widget._refresh(parameters.value);
        });
        this.when("done").toSelf().execute(function(parameters) {
            widget._done(parameters.value);
        });
    }
};