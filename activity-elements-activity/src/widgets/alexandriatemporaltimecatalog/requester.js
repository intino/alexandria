var AlexandriaTemporalTimeCatalogBehaviors = AlexandriaTemporalTimeCatalogBehaviors || {};

AlexandriaTemporalTimeCatalogBehaviors.Requester = {

    selectGrouping : function(value) {
    	this.carry("selectGrouping", { "value" : value });
    },
    clearFilter : function() {
    	this.carry("clearFilter");
    },
    timezoneOffset : function(value) {
    	this.carry("timezoneOffset", { "value" : value });
    },
    navigate : function(value) {
    	this.carry("navigate", { "value" : value });
    }

};