import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractFrameExamplesMold from "../../../gen/displays/templates/AbstractFrameExamplesMold";
import FrameExamplesMoldNotifier from "../../../gen/displays/notifiers/FrameExamplesMoldNotifier";
import FrameExamplesMoldRequester from "../../../gen/displays/requesters/FrameExamplesMoldRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';

const styles = theme => ({});

class FrameExamplesMold extends AbstractFrameExamplesMold {

	constructor(props) {
		super(props);
		this.notifier = new FrameExamplesMoldNotifier(this);
		this.requester = new FrameExamplesMoldRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(withSnackbar(FrameExamplesMold));
DisplayFactory.register("FrameExamplesMold", withStyles(styles, { withTheme: true })(withSnackbar(FrameExamplesMold)));