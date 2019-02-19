import React from "react";
import AbstractPanels from "../../gen/displays/components/AbstractPanels";
import PanelsNotifier from "../../gen/displays/notifiers/PanelsNotifier";
import PanelsRequester from "../../gen/displays/requesters/PanelsRequester";

export default class Panels extends AbstractPanels {

	constructor(props) {
		super(props);
		this.notifier = new PanelsNotifier(this);
		this.requester = new PanelsRequester(this);
	};

	render() {
		return (
			<React.Fragment></React.Fragment>
		);
	};


}