import React from "react";
import AbstractPageDisplay from "../../gen/displays/AbstractPageDisplay";
import PageDisplayNotifier from "../../gen/displays/notifiers/PageDisplayNotifier";
import PageDisplayRequester from "../../gen/displays/requesters/PageDisplayRequester";

export default class PageDisplay extends AbstractPageDisplay {

	constructor(props) {
		super(props);
		this.notifier = new PageDisplayNotifier(this);
		this.requester = new PageDisplayRequester(this);
	};

	render() {
		return (
			<React.Fragment>
			</React.Fragment>
		);
	};


}