import React from "react";
import Component from "../Component";
import GroupingNotifier from "../../../gen/displays/notifiers/GroupingNotifier";
import GroupingRequester from "../../../gen/displays/requesters/GroupingRequester";

export default class Grouping extends Component {

	constructor(props) {
		super(props);
		this.notifier = new GroupingNotifier(this);
		this.requester = new GroupingRequester(this);
	};

	render() {
		return (
			<React.Fragment>
			</React.Fragment>
		);
	};


}