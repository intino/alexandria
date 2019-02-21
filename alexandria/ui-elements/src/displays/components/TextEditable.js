import React from "react";
import AbstractTextEditable from "../../../gen/displays/components/AbstractTextEditable";
import TextEditableNotifier from "../../../gen/displays/notifiers/TextEditableNotifier";
import TextEditableRequester from "../../../gen/displays/requesters/TextEditableRequester";

export default class TextEditable extends AbstractTextEditable {
	state = {
		value : ""
	};

	constructor(props) {
		super(props);
		this.notifier = new TextEditableNotifier(this);
		this.requester = new TextEditableRequester(this);
	};

	handleChange(e) {
		this.requester.notifyChange(e.target.value);
		this.setState({ value: e.target.value });
	}

	handleKeypress(e) {
		this.requester.notifyKeyPress({ keyCode: e.key, value: e.target.value });
		this.setState({ value: e.target.value });
	}

	render() {
		return (
			<input type="text" value={this.state.value}
				   onChange={this.handleChange.bind(this)}
				   onKeyPress={this.handleKeypress.bind(this)}></input>
		);
	};

	refresh = (value) => {
		this.setState({ "value": value });
	};
}