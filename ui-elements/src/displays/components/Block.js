import React from "react";
import Component from "../Component";
import BlockNotifier from "../../../gen/displays/notifiers/BlockNotifier";
import BlockRequester from "../../../gen/displays/requesters/BlockRequester";

export default class Block extends Component {

	constructor(props) {
		super(props);
		this.notifier = new BlockNotifier(this);
		this.requester = new BlockRequester(this);
	};

	render() {
		return (
			<React.Fragment>
			</React.Fragment>
		);
	};


}