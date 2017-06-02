var KonosDialogWidgetBehaviors = KonosDialogWidgetBehaviors || {};

KonosDialogWidgetBehaviors.Requester = {

    update : function(value) {
    	this.carry("update", { "value" : value });
    },
    execute : function() {
    	this.carry("execute");
    },
    uploadResource : function(value) {
        this.carry("uploadResource", { "value": value });
    },
    removeResource : function(value) {
        this.carry("removeResource", { "value": value });
    },
    downloadResource : function(name) {
        this.download("downloadResource", { "value" : name });
    }
};