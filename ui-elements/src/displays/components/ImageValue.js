import React from "react";
import Component from "../Component";
import ImageValueNotifier from "../../../gen/displays/notifiers/ImageValueNotifier";
import ImageValueRequester from "../../../gen/displays/requesters/ImageValueRequester";

export default class ImageValue extends Component {

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