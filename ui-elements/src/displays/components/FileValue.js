import React from "react";
import Component from "../Component";
import FileValueNotifier from "../../../gen/displays/notifiers/FileValueNotifier";
import FileValueRequester from "../../../gen/displays/requesters/FileValueRequester";

export default class FileValue extends Component {

	constructor(props) {
		super(props);
		this.notifier = new FileValueNotifier(this);
		this.requester = new FileValueRequester(this);
	};

	render() {
		return (
			<React.Fragment>
			</React.Fragment>
		);
	};

	update = (value) => {
	};
}