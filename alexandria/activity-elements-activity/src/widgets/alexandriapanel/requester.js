var AlexandriaPanelBehaviors = AlexandriaPanelBehaviors || {};

AlexandriaPanelBehaviors.Requester = {

    openView : function(value) {
    	this.carry("openView", { "value" : value });
    },
    openItem : function(value) {
    	this.carry("openItem", { "value" : value });
    },
    navigate : function(value) {
    	this.carry("navigate", { "value" : value });
    }

};