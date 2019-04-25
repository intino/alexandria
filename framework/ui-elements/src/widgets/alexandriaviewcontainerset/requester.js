var AlexandriaViewContainerSetBehaviors = AlexandriaViewContainerSetBehaviors || {};

AlexandriaViewContainerSetBehaviors.Requester = {

    home : function() {
    	this.carry("home");
    },
    openItem : function(value) {
    	this.carry("openItem", { "value" : value });
    }

};