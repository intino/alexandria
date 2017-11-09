var AlexandriaItemBehaviors = AlexandriaItemBehaviors || {};

AlexandriaItemBehaviors.Requester = {

    itemStampsReady : function(value) {
    	this.carry("itemStampsReady", { "value" : value });
    },
    selectItem : function(value) {
    	this.carry("selectItem", { "value" : value });
    },
    selectElement : function(value) {
    	this.carry("selectElement", { "value" : value });
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
    exportItemOperation : function(value) {
    	this.download("exportItemOperation", { "value" : value });
    },
    saveItem : function(value) {
    	this.carry("saveItem", { "value" : value });
    }

};