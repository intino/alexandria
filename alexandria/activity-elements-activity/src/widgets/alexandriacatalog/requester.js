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
    navigate : function(value) {
    	this.carry("navigate", { "value" : value });
    },
    navigateMain : function() {
    	this.carry("navigateMain");
    }

};