import React from "react";
import AbstractAppBar from "../../../gen/displays/components/AbstractAppBar";
import AppBarNotifier from "../../../gen/displays/notifiers/AppBarNotifier";
import AppBarRequester from "../../../gen/displays/requesters/AppBarRequester";

export default class AppBar extends AbstractAppBar {

	constructor(props) {
		super(props);
		this.notifier = new AppBarNotifier(this);
		this.requester = new AppBarRequester(this);
	};

	render() {
		return (
			<React.Fragment>
			</React.Fragment>
		);
	};


}