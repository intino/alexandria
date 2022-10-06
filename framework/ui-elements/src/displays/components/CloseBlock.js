import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractCloseBlock from "../../../gen/displays/components/AbstractCloseBlock";
import CloseBlockNotifier from "../../../gen/displays/notifiers/CloseBlockNotifier";
import CloseBlockRequester from "../../../gen/displays/requesters/CloseBlockRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';

const styles = theme => ({});

class CloseBlock extends AbstractCloseBlock {

	constructor(props) {
		super(props);
		this.notifier = new CloseBlockNotifier(this);
		this.requester = new CloseBlockRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(withSnackbar(CloseBlock));
DisplayFactory.register("CloseBlock", withStyles(styles, { withTheme: true })(withSnackbar(CloseBlock)));