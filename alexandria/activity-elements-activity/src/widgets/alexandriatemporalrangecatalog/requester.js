var AlexandriaTemporalRangeCatalogBehaviors = AlexandriaTemporalRangeCatalogBehaviors || {};

AlexandriaTemporalRangeCatalogBehaviors.Requester = {

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
    },
    navigateMain : function() {
    	this.carry("navigateMain");
    },
    openView : function(value) {
    	this.carry("openView", { "value" : value });
    },
    openItem : function(value) {
    	this.carry("openItem", { "value" : value });
    }

};