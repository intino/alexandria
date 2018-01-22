var AlexandriaPageContainerBehaviors = AlexandriaPageContainerBehaviors || {};

AlexandriaPageContainerBehaviors.NotifierListener = {

    listenToDisplay : function() {
		if (this.display == null) return;
        var widget = this;
        this.when("refreshLocation").toSelf().execute(function(parameters) {
        	widget._refreshLocation(parameters.value);
        });
    }
};