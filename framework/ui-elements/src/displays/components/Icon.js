import React from "react";
import AbstractIcon from "../../../gen/displays/components/AbstractIcon";
import IconNotifier from "../../../gen/displays/notifiers/IconNotifier";
import IconRequester from "../../../gen/displays/requesters/IconRequester";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

export default class Icon extends AbstractIcon {

	constructor(props) {
		super(props);
		this.notifier = new IconNotifier(this);
		this.requester = new IconRequester(this);
	};

	render() {
		return (
			<React.Fragment>
			</React.Fragment>
		);
	};

}

DisplayFactory.register("Icon", Icon);