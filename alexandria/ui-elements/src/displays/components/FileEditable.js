import React from "react";
import AbstractFile from "../../../gen/displays/components/AbstractFile";
import FileNotifier from "../../../gen/displays/notifiers/FileValueNotifier";
import FileRequester from "../../../gen/displays/requesters/FileValueRequester";

export default class FileEditable extends AbstractFile {
	state = {
		value : ""
	};

	constructor(props) {
		super(props);
		this.notifier = new FileNotifier(this);
		this.requester = new FileRequester(this);
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