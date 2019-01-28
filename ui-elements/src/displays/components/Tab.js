import React from "react";
import Component from "../Component";
import TabNotifier from "../../../gen/displays/notifiers/TabNotifier";
import TabRequester from "../../../gen/displays/requesters/TabRequester";

export default class Tab extends Component {

	constructor(props) {
		super(props);
		this.notifier = new TabNotifier(this);
		this.requester = new TabRequester(this);
	};

	render() {
		return (
			<React.Fragment>
			</React.Fragment>
		);
	};


}