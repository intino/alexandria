var AlexandriaTabLayoutBehaviors = AlexandriaTabLayoutBehaviors || {};

AlexandriaTabLayoutBehaviors.Requester = {

    home : function() {
    	this.carry("home");
    },
    openItem : function(value) {
    	this.carry("openItem", { "value" : value });
    },
    logout : function() {
    	this.carry("logout");
    }

};