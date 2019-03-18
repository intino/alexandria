import React from "react";
import ReactDOM from "react-dom";
import AbstractEmbeddedDisplay from "../../../gen/displays/components/AbstractEmbeddedDisplay";
import EmbeddedDisplayNotifier from "../../../gen/displays/notifiers/EmbeddedDisplayNotifier";
import EmbeddedDisplayRequester from "../../../gen/displays/requesters/EmbeddedDisplayRequester";

export default class EmbeddedDisplay extends AbstractEmbeddedDisplay {
	state = {
		display : null
	};

	constructor(props) {
		super(props);
		this.notifier = new EmbeddedDisplayNotifier(this);
		this.requester = new EmbeddedDisplayRequester(this);
	};

	render() {
		return (<React.Fragment>{this.renderInstances()}</React.Fragment>);
	};

}