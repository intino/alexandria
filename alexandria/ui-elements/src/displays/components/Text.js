import React from "react";
import AbstractText from "../../gen/displays/components/AbstractText";
import TextNotifier from "../../gen/displays/notifiers/TextNotifier";
import TextRequester from "../../gen/displays/requesters/TextRequester";

export default class Text extends AbstractText {

	constructor(props) {
		super(props);
		this.notifier = new TextNotifier(this);
		this.requester = new TextRequester(this);
	};

	render() {
		return (
			<React.Fragment></React.Fragment>
		);
	};

	refresh = (value) => {
	};
}