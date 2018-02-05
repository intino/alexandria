var AlexandriaDialogBehaviors = AlexandriaDialogBehaviors || {};

AlexandriaDialogBehaviors.Requester = {

    saveValue : function(value) {
        this.carry("saveValue", { "value" : value });
    },
    addValue : function(value) {
    	this.carry("addValue", { "value" : value });
    },
    removeValue : function(value) {
        this.carry("removeValue", { "value": value });
    },
    execute : function(value) {
    	this.carry("execute", { "value": value });
    },
    uploadResource : function(value) {
        this.carry("uploadResource", { "value": value });
    },
    downloadResource : function(name) {
        this.download("downloadResource", { "value" : name });
    }
};