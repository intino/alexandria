var AlexandriaCatalogMapViewBehaviors = AlexandriaCatalogMapViewBehaviors || {};

AlexandriaCatalogMapViewBehaviors.Requester = {

    location : function(value) {
    	this.carry("location", { "value" : value });
    },
    page : function(value) {
    	this.carry("page", { "value" : value });
    },
    openElement : function(value) {
    	this.carry("openElement", { "value" : value });
    },
    loadItem : function(value) {
    	this.carry("loadItem", { "value" : value });
    },
    openItem : function(value) {
    	this.carry("openItem", { "value" : value });
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
    }

};