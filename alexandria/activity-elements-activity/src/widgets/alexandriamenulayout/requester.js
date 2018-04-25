var AlexandriaMenuLayoutBehaviors = AlexandriaMenuLayoutBehaviors || {};

AlexandriaMenuLayoutBehaviors.Requester = {

    logout : function() {
    	this.carry("logout");
    },
    openItem : function(value) {
    	this.carry("openItem", { "value" : value });
    },
    showHome : function() {
    	this.carry("showHome");
    }

};