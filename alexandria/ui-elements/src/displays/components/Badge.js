import React from "react";
import AbstractBadge from "../../../gen/displays/components/AbstractBadge";
import BadgeNotifier from "../../../gen/displays/notifiers/BadgeNotifier";
import BadgeRequester from "../../../gen/displays/requesters/BadgeRequester";

export default class Badge extends AbstractBadge {

	constructor(props) {
		super(props);
		this.notifier = new BadgeNotifier(this);
		this.requester = new BadgeRequester(this);
	};

	render() {
		return (
			<React.Fragment>
			</React.Fragment>
		);
	};


}