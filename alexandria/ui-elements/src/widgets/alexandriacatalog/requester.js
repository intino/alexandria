var AlexandriaCatalogBehaviors = AlexandriaCatalogBehaviors || {};

AlexandriaCatalogBehaviors.Requester = {

    selectGrouping : function(value) {
    	this.carry("selectGrouping", { "value" : value });
    },
    deleteGroupingGroup : function(value) {
    	this.carry("deleteGroupingGroup", { "value" : value });
    },
    clearFilter : function() {
    	this.carry("clearFilter");
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