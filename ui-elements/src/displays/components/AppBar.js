import React from "react";
import Component from "../Component";
import AppBarNotifier from "../../../gen/displays/notifiers/AppBarNotifier";
import AppBarRequester from "../../../gen/displays/requesters/AppBarRequester";

export default class AppBar extends Component {

	constructor(props) {
		super(props);
		this.notifier = new AppBarNotifier(this);
		this.requester = new AppBarRequester(this);
	};

	render() {
		return (
			<React.Fragment>{this.props.children}</React.Fragment>
		);
	};


}