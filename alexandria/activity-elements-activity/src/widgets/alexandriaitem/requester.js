var AlexandriaItemBehaviors = AlexandriaItemBehaviors || {};

AlexandriaItemBehaviors.Requester = {

    itemStampsReady : function(value) {
    	this.carry("itemStampsReady", { "value" : value });
    },
    openItem : function(value) {
    	this.carry("openItem", { "value" : value });
    },
    openElement : function(value) {
    	this.carry("openElement", { "value" : value });
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
    exportItemOperation : function(value) {
    	this.download("exportItemOperation", { "value" : value });
    },
    changeItem : function(value) {
    	this.carry("changeItem", { "value" : value });
    },
    validateItem : function(value) {
    	this.carry("validateItem", { "value" : value });
    }

};