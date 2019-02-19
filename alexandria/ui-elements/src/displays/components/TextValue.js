import React from "react";
import AbstractTextValue from "../../../gen/displays/components/AbstractTextValue";
import ValueNotifier from "../../../gen/displays/notifiers/ValueNotifier";
import ValueRequester from "../../../gen/displays/requesters/ValueRequester";

export default class TextValueInput extends AbstractTextValue {
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
			<React.Fragment>
				{ this.props.label !== "" ? <div>{this.props.label}</div> : null }
				<div>{this.state.value}</div>
			</React.Fragment>
		);
	};

	refresh = (value) => {
		this.setState({ "value": value });
	};
}