import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractCloseDialog from "../../../gen/displays/components/AbstractCloseDialog";
import CloseDialogNotifier from "../../../gen/displays/notifiers/CloseDialogNotifier";
import CloseDialogRequester from "../../../gen/displays/requesters/CloseDialogRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';
import Actionable from "./Actionable";

const styles = theme => ({});

class CloseDialog extends AbstractCloseDialog {

	constructor(props) {
		super(props);
		this.notifier = new CloseDialogNotifier(this);
		this.requester = new CloseDialogRequester(this);
	};

}

export default withStyles(Actionable.Styles, { withTheme: true })(CloseDialog);
DisplayFactory.register("CloseDialog", withStyles(Actionable.Styles, { withTheme: true })(CloseDialog));