import React from "react";
import AbstractEmbeddedDisplay from "../../../gen/displays/components/AbstractEmbeddedDisplay";
import EmbeddedDisplayNotifier from "../../../gen/displays/notifiers/EmbeddedDisplayNotifier";
import EmbeddedDisplayRequester from "../../../gen/displays/requesters/EmbeddedDisplayRequester";

export default class EmbeddedDisplay extends AbstractEmbeddedDisplay {
	state = {
		properties : []
	};

	constructor(props) {
		super(props);
		this.notifier = new EmbeddedDisplayNotifier(this);
		this.requester = new EmbeddedDisplayRequester(this);
	};

	render() {
		return (<React.Fragment>{this.props.children}</React.Fragment>);
	}

}