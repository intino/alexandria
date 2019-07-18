import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractDividerExamplesMold from "../../../gen/displays/templates/AbstractDividerExamplesMold";
import DividerExamplesMoldNotifier from "../../../gen/displays/notifiers/DividerExamplesMoldNotifier";
import DividerExamplesMoldRequester from "../../../gen/displays/requesters/DividerExamplesMoldRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';

const styles = theme => ({});

class DividerExamplesMold extends AbstractDividerExamplesMold {

	constructor(props) {
		super(props);
		this.notifier = new DividerExamplesMoldNotifier(this);
		this.requester = new DividerExamplesMoldRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(withSnackbar(DividerExamplesMold));
DisplayFactory.register("DividerExamplesMold", withStyles(styles, { withTheme: true })(withSnackbar(DividerExamplesMold)));