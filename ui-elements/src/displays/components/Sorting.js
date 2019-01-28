import React from "react";
import Component from "../Component";
import SortingNotifier from "../../../gen/displays/notifiers/SortingNotifier";
import SortingRequester from "../../../gen/displays/requesters/SortingRequester";

export default class Sorting extends Component {

	constructor(props) {
		super(props);
		this.notifier = new SortingNotifier(this);
		this.requester = new SortingRequester(this);
	};

	render() {
		return (
			<React.Fragment>
			</React.Fragment>
		);
	};


}