import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractAlertDialog from "../../../gen/displays/components/AbstractAlertDialog";
import AlertDialogNotifier from "../../../gen/displays/notifiers/AlertDialogNotifier";
import AlertDialogRequester from "../../../gen/displays/requesters/AlertDialogRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';

const styles = theme => ({});

class AlertDialog extends AbstractAlertDialog {

	constructor(props) {
		super(props);
		this.notifier = new AlertDialogNotifier(this);
		this.requester = new AlertDialogRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(withSnackbar(AlertDialog));
DisplayFactory.register("AlertDialog", withStyles(styles, { withTheme: true })(withSnackbar(AlertDialog)));