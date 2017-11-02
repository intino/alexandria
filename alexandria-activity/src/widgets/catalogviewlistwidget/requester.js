var CatalogViewListWidgetBehaviors = CatalogViewListWidgetBehaviors || {};

CatalogViewListWidgetBehaviors.Requester = {

    selectView : function(value) {
    	this.carry("selectView", { "value" : value });
    }

};