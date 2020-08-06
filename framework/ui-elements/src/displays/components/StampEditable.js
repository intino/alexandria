import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractStampEditable from "../../../gen/displays/components/AbstractStampEditable";
import StampEditableNotifier from "../../../gen/displays/notifiers/StampEditableNotifier";
import StampEditableRequester from "../../../gen/displays/requesters/StampEditableRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';

const styles = theme => ({});

class StampEditable extends AbstractStampEditable {

	constructor(props) {
		super(props);
		this.notifier = new StampEditableNotifier(this);
		this.requester = new StampEditableRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(withSnackbar(StampEditable));
DisplayFactory.register("StampEditable", withStyles(styles, { withTheme: true })(withSnackbar(StampEditable)));