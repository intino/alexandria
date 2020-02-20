import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractCloseDrawer from "../../../gen/displays/components/AbstractCloseDrawer";
import CloseDrawerNotifier from "../../../gen/displays/notifiers/CloseDrawerNotifier";
import CloseDrawerRequester from "../../../gen/displays/requesters/CloseDrawerRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';
import Actionable from "./Actionable";

const styles = theme => ({});

class CloseDrawer extends AbstractCloseDrawer {

	constructor(props) {
		super(props);
		this.notifier = new CloseDrawerNotifier(this);
		this.requester = new CloseDrawerRequester(this);
	};

}

export default withStyles(Actionable.Styles, { withTheme: true })(withSnackbar(CloseDrawer));
DisplayFactory.register("CloseDrawer", withStyles(Actionable.Styles, { withTheme: true })(withSnackbar(CloseDrawer)));