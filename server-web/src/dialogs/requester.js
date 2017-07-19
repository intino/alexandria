var KonosDialogWidgetBehaviors = KonosDialogWidgetBehaviors || {};

KonosDialogWidgetBehaviors.Requester = {

    saveValue : function(value) {
        this.carry("saveValue", { "value" : value });
    },
    addValue : function(value) {
    	this.carry("addValue", { "value" : value });
    },
    removeValue : function(value) {
        this.carry("removeValue", { "value": value });
    },
    execute : function() {
    	this.carry("execute");
    },
    uploadResource : function(value) {
        this.carry("uploadResource", { "value": value });
    },
    downloadResource : function(name) {
        this.download("downloadResource", { "value" : name });
    }
};