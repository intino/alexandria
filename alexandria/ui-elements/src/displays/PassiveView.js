import React from "react";
import AbstractPassiveView from "../../gen/displays/AbstractPassiveView";
import PassiveViewNotifier from "../../../gen/displays/notifiers/PassiveViewNotifier";
import PassiveViewRequester from "../../../gen/displays/requesters/PassiveViewRequester";

export default class PassiveView extends AbstractPassiveView {

	constructor(props) {
		super(props);
		this.notifier = new PassiveViewNotifier(this);
		this.requester = new PassiveViewRequester(this);
	};

	render() {
		return (
			<React.Fragment>
			</React.Fragment>
		);
	};


}