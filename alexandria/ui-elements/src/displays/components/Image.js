import React from "react";
import AbstractImage from "../../gen/displays/components/AbstractImage";
import ImageNotifier from "../../gen/displays/notifiers/ImageNotifier";
import ImageRequester from "../../gen/displays/requesters/ImageRequester";

export default class Image extends AbstractImage {

	constructor(props) {
		super(props);
		this.notifier = new ImageNotifier(this);
		this.requester = new ImageRequester(this);
	};

	render() {
		return (
			<React.Fragment></React.Fragment>
		);
	};

	refreshImage = (value) => {
	};
}