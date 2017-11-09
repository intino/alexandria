var AlexandriaMenuLayoutBehaviors = AlexandriaMenuLayoutBehaviors || {};

AlexandriaMenuLayoutBehaviors.Requester = {

    logout : function() {
    	this.carry("logout");
    },
    selectItem : function(value) {
    	this.carry("selectItem", { "value" : value });
    }

};