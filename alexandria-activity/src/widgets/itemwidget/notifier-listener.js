var ItemWidgetBehaviors = ItemWidgetBehaviors || {};

ItemWidgetBehaviors.NotifierListener = {

    listenToDisplay : function() {
        var widget = this;
        this.when("refresh").toSelf().execute(function(parameters) {
        	widget._refresh(parameters.value);
        });
        this.when("refreshMode").toSelf().execute(function(parameters) {
        	widget._refreshMode(parameters.value);
        });
        this.when("refreshEmptyMessage").toSelf().execute(function(parameters) {
        	widget._refreshEmptyMessage(parameters.value);
        });
    }
};