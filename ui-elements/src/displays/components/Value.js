import React from "react";
import Component from "../Component";
import ValueNotifier from "../../../gen/displays/notifiers/ValueNotifier";
import ValueRequester from "../../../gen/displays/requesters/ValueRequester";

export default class Value extends Component {

	constructor(props) {
		super(props);
		this.notifier = new ValueNotifier(this);
		this.requester = new ValueRequester(this);
	};

	render() {
		return (
			<React.Fragment>
			</React.Fragment>
		);
	};

	update = (value) => {
	};
}