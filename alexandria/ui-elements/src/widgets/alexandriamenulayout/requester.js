var AlexandriaMenuLayoutBehaviors = AlexandriaMenuLayoutBehaviors || {};

AlexandriaMenuLayoutBehaviors.Requester = {

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