var AlexandriaPanelBehaviors = AlexandriaPanelBehaviors || {};

AlexandriaPanelBehaviors.Requester = {

    selectView : function(value) {
    	this.carry("selectView", { "value" : value });
    },
    navigate : function(value) {
    	this.carry("navigate", { "value" : value });
    }

};