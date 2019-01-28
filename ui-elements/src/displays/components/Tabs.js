import React from "react";
import Component from "../Component";
import TabsNotifier from "../../../gen/displays/notifiers/TabsNotifier";
import TabsRequester from "../../../gen/displays/requesters/TabsRequester";

export default class Tabs extends Component {

	constructor(props) {
		super(props);
		this.notifier = new TabsNotifier(this);
		this.requester = new TabsRequester(this);
	};

	render() {
		return (
			<React.Fragment>
			</React.Fragment>
		);
	};


}