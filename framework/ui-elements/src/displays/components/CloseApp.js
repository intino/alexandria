import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractCloseApp from "../../../gen/displays/components/AbstractCloseApp";
import CloseAppNotifier from "../../../gen/displays/notifiers/CloseAppNotifier";
import CloseAppRequester from "../../../gen/displays/requesters/CloseAppRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';
import Actionable from "./Actionable";

const styles = theme => ({});

class CloseApp extends AbstractCloseApp {

	constructor(props) {
		super(props);
		this.notifier = new CloseAppNotifier(this);
		this.requester = new CloseAppRequester(this);
	};

	close = () => {
	    window.close();
	};

}

export default withStyles(Actionable.Styles, { withTheme: true })(withSnackbar(CloseApp));
DisplayFactory.register("CloseApp", withStyles(Actionable.Styles, { withTheme: true })(withSnackbar(CloseApp)));