import React from "react";
import AbstractImageValue from "../../../gen/displays/components/AbstractImageValue";
import ImageValueNotifier from "../../../gen/displays/notifiers/ImageValueNotifier";
import ImageValueRequester from "../../../gen/displays/requesters/ImageValueRequester";

export default class ImageValue extends AbstractImageValue {

	constructor(props) {
		super(props);
		this.notifier = new ImageValueNotifier(this);
		this.requester = new ImageValueRequester(this);
	};

	render() {
		return (
			<React.Fragment>
			</React.Fragment>
		);
	};

	refreshImage = (value) => {
	};
}