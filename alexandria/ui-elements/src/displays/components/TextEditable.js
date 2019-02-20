import React from "react";
import AbstractText from "../../../gen/displays/components/AbstractText";
import TextNotifier from "../../../gen/displays/notifiers/TextNotifier";
import TextRequester from "../../../gen/displays/requesters/TextRequester";

export default class TextEditable extends AbstractText {
	state = {
		value : ""
	};

	constructor(props) {
		super(props);
		this.notifier = new TextNotifier(this);
		this.requester = new TextRequester(this);
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