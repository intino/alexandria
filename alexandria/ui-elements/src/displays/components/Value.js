import React from "react";
import AbstractValue from "../../../gen/displays/components/AbstractValue";

export default class Value extends AbstractValue {
	state = {
		value : ""
	};

	update = (value) => {
		this.setState({ "value": value });
	};

	render() {
		<div>{this.state.value}</div>
	}
}