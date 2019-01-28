import React from "react";
import Component from "../Component";
import CatalogNotifier from "../../../gen/displays/notifiers/CatalogNotifier";
import CatalogRequester from "../../../gen/displays/requesters/CatalogRequester";

export default class Catalog extends Component {

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