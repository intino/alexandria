import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractPortal from "../../../gen/displays/components/AbstractPortal";
import PortalNotifier from "../../../gen/displays/notifiers/PortalNotifier";
import PortalRequester from "../../../gen/displays/requesters/PortalRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';

const styles = theme => ({});

class Portal extends AbstractPortal {

	constructor(props) {
		super(props);
		this.notifier = new PortalNotifier(this);
		this.requester = new PortalRequester(this);
	};

	render() {
		return (<div>{this.renderInstances()}</div>);
	};
}

export default withStyles(styles, { withTheme: true })(withSnackbar(Portal));
DisplayFactory.register("Portal", withStyles(styles, { withTheme: true })(withSnackbar(Portal)));