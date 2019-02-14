import React from "react";
import AbstractRoot from "../../gen/displays/AbstractRoot";
import RootNotifier from "../../gen/displays/notifiers/RootNotifier";
import RootRequester from "../../gen/displays/requesters/RootRequester";

export default class Root extends AbstractRoot {

	constructor(props) {
		super(props);
		this.notifier = new RootNotifier(this);
		this.requester = new RootRequester(this);
	};

	render() {
		return (
			<React.Fragment>
			</React.Fragment>
		);
	};


}