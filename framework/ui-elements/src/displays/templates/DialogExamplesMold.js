import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractDialogExamplesMold from "../../../gen/displays/templates/AbstractDialogExamplesMold";
import DialogExamplesMoldNotifier from "../../../gen/displays/notifiers/DialogExamplesMoldNotifier";
import DialogExamplesMoldRequester from "../../../gen/displays/requesters/DialogExamplesMoldRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';

const styles = theme => ({});

class DialogExamplesMold extends AbstractDialogExamplesMold {

	constructor(props) {
		super(props);
		this.notifier = new DialogExamplesMoldNotifier(this);
		this.requester = new DialogExamplesMoldRequester(this);
	};

}

export default withStyles(styles, { withTheme: true })(withSnackbar(DialogExamplesMold));
DisplayFactory.register("DialogExamplesMold", withStyles(styles, { withTheme: true })(withSnackbar(DialogExamplesMold)));