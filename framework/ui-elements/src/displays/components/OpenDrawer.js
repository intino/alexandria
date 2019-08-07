import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractOpenDrawer from "../../../gen/displays/components/AbstractOpenDrawer";
import OpenDrawerNotifier from "../../../gen/displays/notifiers/OpenDrawerNotifier";
import OpenDrawerRequester from "../../../gen/displays/requesters/OpenDrawerRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';

const styles = theme => ({});

class OpenDrawer extends AbstractOpenDrawer {

	constructor(props) {
		super(props);
		this.notifier = new OpenDrawerNotifier(this);
		this.requester = new OpenDrawerRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(withSnackbar(OpenDrawer));
DisplayFactory.register("OpenDrawer", withStyles(styles, { withTheme: true })(withSnackbar(OpenDrawer)));