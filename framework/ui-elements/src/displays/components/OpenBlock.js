import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractOpenBlock from "../../../gen/displays/components/AbstractOpenBlock";
import OpenBlockNotifier from "../../../gen/displays/notifiers/OpenBlockNotifier";
import OpenBlockRequester from "../../../gen/displays/requesters/OpenBlockRequester";
import Operation from "./Operation"
import { withSnackbar } from 'notistack';
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

class OpenBlock extends AbstractOpenBlock {

	constructor(props) {
		super(props);
		this.notifier = new OpenBlockNotifier(this);
		this.requester = new OpenBlockRequester(this);
	};

}

export default withStyles(Operation.Styles, { withTheme: true })(withSnackbar(OpenBlock));
DisplayFactory.register("OpenBlock", withStyles(Operation.Styles, { withTheme: true })(withSnackbar(OpenBlock)));