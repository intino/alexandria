var AlexandriaDialogContainerBehaviors = AlexandriaDialogContainerBehaviors || {};

AlexandriaDialogContainerBehaviors.Requester = {

    dialogAssertionMade : function(value) {
    	this.carry("dialogAssertionMade", { "value" : value });
    }

};