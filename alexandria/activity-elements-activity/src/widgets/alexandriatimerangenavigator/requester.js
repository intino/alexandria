var AlexandriaTimeRangeNavigatorBehaviors = AlexandriaTimeRangeNavigatorBehaviors || {};

AlexandriaTimeRangeNavigatorBehaviors.Requester = {

    selectScale : function(value) {
    	this.carry("selectScale", { "value" : value });
    },
    selectFrom : function(value) {
    	this.carry("selectFrom", { "value" : value });
    },
    selectTo : function(value) {
    	this.carry("selectTo", { "value" : value });
    },
    move : function(value) {
    	this.carry("move", { "value" : value });
    },
    moveNext : function() {
    	this.carry("moveNext");
    },
    movePrevious : function() {
    	this.carry("movePrevious");
    }

};