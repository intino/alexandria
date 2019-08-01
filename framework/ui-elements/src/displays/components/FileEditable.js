import React from "react";
import AbstractFile from "../../../gen/displays/components/AbstractFile";
import FileEditableNotifier from "../../../gen/displays/notifiers/FileEditableNotifier";
import FileEditableRequester from "../../../gen/displays/requesters/FileEditableRequester";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

export default class FileEditable extends AbstractFile {
	state = {
		value : "",
		readonly : this.props.readonly
	};

	constructor(props) {
		super(props);
		this.notifier = new FileEditableNotifier(this);
		this.requester = new FileEditableRequester(this);
	};

	handleChange(e) {
		this.requester.notifyChange(e.target.files[0]);
		this.setState({ value: e.target.value });
	}

	render() {
		return (
			<input type="file" value={this.state.value} disabled={this.state.readonly ? "disabled" : undefined}
				   onChange={this.handleChange.bind(this)}></input>
		);
	};

	refresh = (value) => {
		this.setState({ "value": value });
	};

	refreshReadonly = (readonly) => {
		this.setState({ readonly });
	};
}

DisplayFactory.register("FileEditable", FileEditable);