import React from "react";
import AbstractFileValue from "../../../gen/displays/components/AbstractFileValue";
import FileValueNotifier from "../../../gen/displays/notifiers/FileValueNotifier";
import FileValueRequester from "../../../gen/displays/requesters/FileValueRequester";

export default class FileValue extends AbstractFileValue {
	state = {
		value : ""
	};

	constructor(props) {
		super(props);
		this.notifier = new FileValueNotifier(this);
		this.requester = new FileValueRequester(this);
	};

	render() {
		return (
			<div>{this.state.value}</div>
		);
	};

	refresh = (value) => {
		this.setState({ "value": value });
	};
}