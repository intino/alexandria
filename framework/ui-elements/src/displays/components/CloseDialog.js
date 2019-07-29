import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractCloseDialog from "../../../gen/displays/components/AbstractCloseDialog";
import CloseDialogNotifier from "../../../gen/displays/notifiers/CloseDialogNotifier";
import CloseDialogRequester from "../../../gen/displays/requesters/CloseDialogRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';

const styles = theme => ({});

class CloseDialog extends AbstractCloseDialog {

	constructor(props) {
		super(props);
		this.notifier = new CloseDialogNotifier(this);
		this.requester = new CloseDialogRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(withSnackbar(CloseDialog));
DisplayFactory.register("CloseDialog", withStyles(styles, { withTheme: true })(withSnackbar(CloseDialog)));