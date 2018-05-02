var AlexandriaCatalogListViewBehaviors = AlexandriaCatalogListViewBehaviors || {};

AlexandriaCatalogListViewBehaviors.Requester = {

    openElement : function(value) {
    	this.carry("openElement", { "value" : value });
    },
    selectItems : function(value) {
    	this.carry("selectItems", { "value" : value });
    },
    renderExpandedPictures : function() {
    	this.carry("renderExpandedPictures");
    },
    itemRefreshed : function(value) {
    	this.carry("itemRefreshed", { "value" : value });
    },
    selectSorting : function(value) {
    	this.carry("selectSorting", { "value" : value });
    },
    page : function(value) {
    	this.carry("page", { "value" : value });
    },
    filter : function(value) {
    	this.carry("filter", { "value" : value });
    },
    executeOperation : function(value) {
    	this.carry("executeOperation", { "value" : value });
    },
    downloadOperation : function(value) {
    	this.download("downloadOperation", { "value" : value });
    },
    openItemDialogOperation : function(value) {
    	this.carry("openItemDialogOperation", { "value" : value });
    },
    openItemCatalogOperation : function(value) {
    	this.carry("openItemCatalogOperation", { "value" : value });
    },
    executeItemTaskOperation : function(value) {
    	this.carry("executeItemTaskOperation", { "value" : value });
    },
    downloadItemOperation : function(value) {
    	this.download("downloadItemOperation", { "value" : value });
    },
    changeItem : function(value) {
    	this.carry("changeItem", { "value" : value });
    },
    validateItem : function(value) {
    	this.carry("validateItem", { "value" : value });
    }

};