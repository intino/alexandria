import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractOpenDrawer from "../../../gen/displays/components/AbstractOpenDrawer";
import OpenDrawerNotifier from "../../../gen/displays/notifiers/OpenDrawerNotifier";
import OpenDrawerRequester from "../../../gen/displays/requesters/OpenDrawerRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';
import Operation from "./Operation";

class OpenDrawer extends AbstractOpenDrawer {

	constructor(props) {
		super(props);
		this.notifier = new OpenDrawerNotifier(this);
		this.requester = new OpenDrawerRequester(this);
	};


}

export default withStyles(Operation.Styles, { withTheme: true })(withSnackbar(OpenDrawer));
DisplayFactory.register("OpenDrawer", withStyles(Operation.Styles, { withTheme: true })(withSnackbar(OpenDrawer)));