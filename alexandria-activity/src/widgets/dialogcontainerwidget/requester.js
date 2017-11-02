var DialogContainerWidgetBehaviors = DialogContainerWidgetBehaviors || {};

DialogContainerWidgetBehaviors.Requester = {

    dialogAssertionMade : function(value) {
    	this.carry("dialogAssertionMade", { "value" : value });
    }

};