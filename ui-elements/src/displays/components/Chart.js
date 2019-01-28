import React from "react";
import Component from "../Component";
import ChartNotifier from "../../../gen/displays/notifiers/ChartNotifier";
import ChartRequester from "../../../gen/displays/requesters/ChartRequester";

export default class Chart extends Component {

	constructor(props) {
		super(props);
		this.notifier = new ChartNotifier(this);
		this.requester = new ChartRequester(this);
	};

	render() {
		return (
			<React.Fragment>
			</React.Fragment>
		);
	};


}