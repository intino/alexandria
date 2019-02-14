import React from "react";
import AbstractDesktop from "../../gen/displays/AbstractDesktop";
import DesktopNotifier from "../../gen/displays/notifiers/DesktopNotifier";
import DesktopRequester from "../../gen/displays/requesters/DesktopRequester";

export default class Desktop extends AbstractDesktop {

	constructor(props) {
		super(props);
		this.notifier = new DesktopNotifier(this);
		this.requester = new DesktopRequester(this);
	};

	render() {
		return (
			<React.Fragment>
			</React.Fragment>
		);
	};


}