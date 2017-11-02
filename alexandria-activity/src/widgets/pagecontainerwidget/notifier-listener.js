var PageContainerWidgetBehaviors = PageContainerWidgetBehaviors || {};

PageContainerWidgetBehaviors.NotifierListener = {

    listenToDisplay : function() {
        var widget = this;
        this.when("refreshLocation").toSelf().execute(function(parameters) {
        	widget._refreshLocation(parameters.value);
        });
    }
};