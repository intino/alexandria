import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractActionableExamplesMold from "../../../gen/displays/templates/AbstractActionableExamplesMold";
import ActionableExamplesMoldNotifier from "../../../gen/displays/notifiers/ActionableExamplesMoldNotifier";
import ActionableExamplesMoldRequester from "../../../gen/displays/requesters/ActionableExamplesMoldRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';

const styles = theme => ({});

class ActionableExamplesMold extends AbstractActionableExamplesMold {

	constructor(props) {
		super(props);
		this.notifier = new ActionableExamplesMoldNotifier(this);
		this.requester = new ActionableExamplesMoldRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(withSnackbar(ActionableExamplesMold));
DisplayFactory.register("ActionableExamplesMold", withStyles(styles, { withTheme: true })(withSnackbar(ActionableExamplesMold)));