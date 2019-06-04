import React from "react";
import AbstractPanel from "../../../gen/displays/components/AbstractPanel";
import PanelNotifier from "../../../gen/displays/notifiers/PanelNotifier";
import PanelRequester from "../../../gen/displays/requesters/PanelRequester";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

export default class Panel extends AbstractPanel {

	constructor(props) {
		super(props);
		this.notifier = new PanelNotifier(this);
		this.requester = new PanelRequester(this);
	};

	render() {
		return (
			<React.Fragment>{this.props.children}</React.Fragment>
		);
	};

}

DisplayFactory.register("Panel", Panel);