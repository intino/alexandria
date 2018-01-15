var AlexandriaCatalogListViewBehaviors = AlexandriaCatalogListViewBehaviors || {};

AlexandriaCatalogListViewBehaviors.Requester = {

    openItem : function(value) {
    	this.carry("openItem", { "value" : value });
    },
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
    createClusterGroup : function(value) {
    	this.carry("createClusterGroup", { "value" : value });
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
    executeItemTaskOperation : function(value) {
    	this.carry("executeItemTaskOperation", { "value" : value });
    },
    downloadItemOperation : function(value) {
    	this.download("downloadItemOperation", { "value" : value });
    },
    saveItem : function(value) {
    	this.carry("saveItem", { "value" : value });
    }

};