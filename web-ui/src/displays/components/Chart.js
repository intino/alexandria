import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractChart from "../../../gen/displays/components/AbstractChart";
import ChartNotifier from "../../../gen/displays/notifiers/ChartNotifier";
import ChartRequester from "../../../gen/displays/requesters/ChartRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class Chart extends AbstractChart {

	constructor(props) {
		super(props);
		this.notifier = new ChartNotifier(this);
		this.requester = new ChartRequester(this);
	};

	showLoading = () => {
	};

	refresh = (value) => {
	};

	refreshError = (value) => {
	};
}

export default withStyles(styles, { withTheme: true })(Chart);
DisplayFactory.register("Chart", withStyles(styles, { withTheme: true })(Chart));