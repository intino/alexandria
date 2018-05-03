var AlexandriaPanelBehaviors = AlexandriaPanelBehaviors || {};

AlexandriaPanelBehaviors.Requester = {

    home : function() {
    	this.carry("home");
    },
    openItem : function(value) {
    	this.carry("openItem", { "value" : value });
    },
    openElement : function(value) {
    	this.carry("openElement", { "value" : value });
    },
    openView : function(value) {
    	this.carry("openView", { "value" : value });
    }

};