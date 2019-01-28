import React from "react";
import Component from "../Component";
import BadgeNotifier from "../../../gen/displays/notifiers/BadgeNotifier";
import BadgeRequester from "../../../gen/displays/requesters/BadgeRequester";

export default class Badge extends Component {

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