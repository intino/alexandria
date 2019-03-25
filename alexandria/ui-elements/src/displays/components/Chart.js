import React from "react";
import AbstractChart from "../../../gen/displays/components/AbstractChart";
import ChartNotifier from "../../../gen/displays/notifiers/ChartNotifier";
import ChartRequester from "../../../gen/displays/requesters/ChartRequester";

export default class Chart extends AbstractChart {

	constructor(props) {
		super(props);
		this.notifier = new ChartNotifier(this);
		this.requester = new ChartRequester(this);
	};

	render() {
		return (
			<React.Fragment>
				<div>hola mundo!!</div>
			</React.Fragment>
		);
	};


}