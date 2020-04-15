import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractOpenPopover from "../../../gen/displays/components/AbstractOpenPopover";
import OpenPopoverNotifier from "../../../gen/displays/notifiers/OpenPopoverNotifier";
import OpenPopoverRequester from "../../../gen/displays/requesters/OpenPopoverRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';

const styles = theme => ({});

class OpenPopover extends AbstractOpenPopover {

	constructor(props) {
		super(props);
		this.notifier = new OpenPopoverNotifier(this);
		this.requester = new OpenPopoverRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(withSnackbar(OpenPopover));
DisplayFactory.register("OpenPopover", withStyles(styles, { withTheme: true })(withSnackbar(OpenPopover)));