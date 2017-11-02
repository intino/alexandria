var CatalogMapViewWidgetBehaviors = CatalogMapViewWidgetBehaviors || {};

CatalogMapViewWidgetBehaviors.Requester = {

    location : function(value) {
    	this.carry("location", { "value" : value });
    },
    page : function(value) {
    	this.carry("page", { "value" : value });
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