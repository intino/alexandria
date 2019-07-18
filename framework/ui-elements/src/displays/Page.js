import React from "react";
import AbstractPage from "../../gen/displays/AbstractPage";
import PageNotifier from "../../gen/displays/notifiers/PageNotifier";
import PageRequester from "../../gen/displays/requesters/PageRequester";

export default class Page extends AbstractPage {

	constructor(props) {
		super(props);
		this.notifier = new PageNotifier(this);
		this.requester = new PageRequester(this);
	};

	render() {
		return (
			<React.Fragment>
			</React.Fragment>
		);
	};


}