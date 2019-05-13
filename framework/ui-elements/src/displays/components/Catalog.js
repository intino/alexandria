import React from "react";
import AbstractCatalog from "../../../gen/displays/components/AbstractCatalog";
import CatalogNotifier from "../../../gen/displays/notifiers/CatalogNotifier";
import CatalogRequester from "../../../gen/displays/requesters/CatalogRequester";

export default class Catalog extends AbstractCatalog {

	constructor(props) {
		super(props);
		this.notifier = new CatalogNotifier(this);
		this.requester = new CatalogRequester(this);
	};

	render() {
		return (
			<React.Fragment>
			</React.Fragment>
		);
	};


}