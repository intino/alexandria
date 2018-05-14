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
    refreshItems : function() {
    	this.carry("refreshItems");
    },
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