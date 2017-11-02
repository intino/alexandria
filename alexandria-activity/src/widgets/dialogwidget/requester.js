var DialogWidgetBehaviors = DialogWidgetBehaviors || {};

DialogWidgetBehaviors.Requester = {

    saveValue : function(value) {
    	this.carry("saveValue", { "value" : value });
    },
    addValue : function(value) {
    	this.carry("addValue", { "value" : value });
    },
    removeValue : function(value) {
    	this.carry("removeValue", { "value" : value });
    },
    execute : function(value) {
    	this.carry("execute", { "value" : value });
    },
    uploadResource : function(value) {
    	this.carry("uploadResource", { "value" : value });
    },
    downloadResource : function(value) {
    	this.download("downloadResource", { "value" : value });
    }

};