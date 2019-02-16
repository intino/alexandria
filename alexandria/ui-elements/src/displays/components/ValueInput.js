import React from "react";
import AbstractValue from "../../../gen/displays/components/AbstractValue";
import ValueNotifier from "../../../gen/displays/notifiers/ValueNotifier";
import ValueRequester from "../../../gen/displays/requesters/ValueRequester";

export default class ValueInput extends AbstractValue {
	state = {
		value : ""
	};

	constructor(props) {
		super(props);
		this.notifier = new ValueNotifier(this);
		this.requester = new ValueRequester(this);
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