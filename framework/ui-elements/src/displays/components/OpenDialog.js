import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractOpenDialog from "../../../gen/displays/components/AbstractOpenDialog";
import OpenDialogNotifier from "../../../gen/displays/notifiers/OpenDialogNotifier";
import OpenDialogRequester from "../../../gen/displays/requesters/OpenDialogRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import Actionable from "./Actionable";

const styles = theme => ({});

class OpenDialog extends AbstractOpenDialog {

	constructor(props) {
		super(props);
		this.notifier = new OpenDialogNotifier(this);
		this.requester = new OpenDialogRequester(this);
	};

}

export default withStyles(Actionable.Styles, { withTheme: true })(OpenDialog);
DisplayFactory.register("OpenDialog", withStyles(Actionable.Styles, { withTheme: true })(OpenDialog));