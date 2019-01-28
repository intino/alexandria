import React from "react";
import Component from "../Component";
import IconNotifier from "../../../gen/displays/notifiers/IconNotifier";
import IconRequester from "../../../gen/displays/requesters/IconRequester";

export default class Icon extends Component {

	constructor(props) {
		super(props);
		this.notifier = new IconNotifier(this);
		this.requester = new IconRequester(this);
	};

	render() {
		return (
			<React.Fragment>
			</React.Fragment>
		);
	};


}