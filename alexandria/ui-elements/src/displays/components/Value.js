import React from "react";
import AbstractValue from "../../../gen/displays/components/AbstractValue";
import ValueNotifier from "../../../gen/displays/notifiers/ValueNotifier";
import ValueRequester from "../../../gen/displays/requesters/ValueRequester";

export default class Value extends AbstractValue {
	state = {
		value : ""
	};

	constructor(props) {
		super(props);
		this.notifier = new ValueNotifier(this);
		this.requester = new ValueRequester(this);
	};

	render() {
		return (
			<div>{this.state.value}</div>
		);
	};

	update = (value) => {
		this.setState({ "value": value });
	};
}