var AlexandriaCatalogMagazineViewBehaviors = AlexandriaCatalogMagazineViewBehaviors || {};

AlexandriaCatalogMagazineViewBehaviors.NotifierListener = {

	properties : {
		_listeningToDisplay : { type: Boolean, value: function() { return false; } }
	},

    listenToDisplay : function() {
		if (this.display == null || this._listeningToDisplay) return;
        var widget = this;

        this._listeningToDisplay = true;
    }
};