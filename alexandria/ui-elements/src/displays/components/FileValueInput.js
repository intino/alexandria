import React from "react";
import AbstractFileValue from "../../../gen/displays/components/AbstractFileValue";
import FileValueNotifier from "../../../gen/displays/notifiers/FileValueNotifier";
import FileValueRequester from "../../../gen/displays/requesters/FileValueRequester";

export default class FileValueInput extends AbstractFileValue {
	state = {
		value : ""
	};

	constructor(props) {
		super(props);
		this.notifier = new FileValueNotifier(this);
		this.requester = new FileValueRequester(this);
	};

	handleChange(e) {
		this.requester.notifyChange(e.target.files[0]);
		this.setState({ value: e.target.value });
	}

	render() {
		return (
			<input type="file" value={this.state.value}
				   onChange={this.handleChange.bind(this)}></input>
		);
	};

	refresh = (value) => {
		this.setState({ "value": value });
	};
}