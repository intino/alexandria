import React from "react";
import AbstractTab from "../../../gen/displays/components/AbstractTab";
import TabNotifier from "../../../gen/displays/notifiers/TabNotifier";
import TabRequester from "../../../gen/displays/requesters/TabRequester";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

export default class Tab extends AbstractTab {

	constructor(props) {
		super(props);
		this.notifier = new TabNotifier(this);
		this.requester = new TabRequester(this);
	};

	render() {
		return (
			<React.Fragment>
			</React.Fragment>
		);
	};

}

DisplayFactory.register("Tab", Tab);