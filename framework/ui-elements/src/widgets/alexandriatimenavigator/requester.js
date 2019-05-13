var AlexandriaTimeNavigatorBehaviors = AlexandriaTimeNavigatorBehaviors || {};

AlexandriaTimeNavigatorBehaviors.Requester = {

    selectScale : function(value) {
    	this.carry("selectScale", { "value" : value });
    },
    selectDate : function(value) {
    	this.carry("selectDate", { "value" : value });
    },
    previousDate : function() {
    	this.carry("previousDate");
    },
    nextDate : function() {
    	this.carry("nextDate");
    },
    play : function() {
    	this.carry("play");
    },
    pause : function() {
    	this.carry("pause");
    },
    lastDate : function() {
    	this.carry("lastDate");
    }

};