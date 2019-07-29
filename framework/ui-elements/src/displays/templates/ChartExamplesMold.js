import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractChartExamplesMold from "../../../gen/displays/templates/AbstractChartExamplesMold";
import ChartExamplesMoldNotifier from "../../../gen/displays/notifiers/ChartExamplesMoldNotifier";
import ChartExamplesMoldRequester from "../../../gen/displays/requesters/ChartExamplesMoldRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class ChartExamplesMold extends AbstractChartExamplesMold {

	constructor(props) {
		super(props);
		this.notifier = new ChartExamplesMoldNotifier(this);
		this.requester = new ChartExamplesMoldRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(ChartExamplesMold);
DisplayFactory.register("ChartExamplesMold", withStyles(styles, { withTheme: true })(ChartExamplesMold));