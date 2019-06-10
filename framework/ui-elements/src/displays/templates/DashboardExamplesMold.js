import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractDashboardExamplesMold from "../../../gen/displays/templates/AbstractDashboardExamplesMold";
import DashboardExamplesMoldNotifier from "../../../gen/displays/notifiers/DashboardExamplesMoldNotifier";
import DashboardExamplesMoldRequester from "../../../gen/displays/requesters/DashboardExamplesMoldRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class DashboardExamplesMold extends AbstractDashboardExamplesMold {

	constructor(props) {
		super(props);
		this.notifier = new DashboardExamplesMoldNotifier(this);
		this.requester = new DashboardExamplesMoldRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(DashboardExamplesMold);
DisplayFactory.register("DashboardExamplesMold", withStyles(styles, { withTheme: true })(DashboardExamplesMold));