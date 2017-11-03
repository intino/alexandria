var AlexandriaDesktopBehaviors = AlexandriaDesktopBehaviors || {};

AlexandriaDesktopBehaviors.NotifierListener = {

    listenToDisplay : function() {
        var widget = this;
        this.when("loading").execute(function(parameters) {
        	widget._loading(parameters.value);
        });
        this.when("loaded").execute(function(parameters) {
        	widget._loaded();
        });
    }
};