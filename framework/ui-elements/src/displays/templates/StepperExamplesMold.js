import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractStepperExamplesMold from "../../../gen/displays/templates/AbstractStepperExamplesMold";
import StepperExamplesMoldNotifier from "../../../gen/displays/notifiers/StepperExamplesMoldNotifier";
import StepperExamplesMoldRequester from "../../../gen/displays/requesters/StepperExamplesMoldRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';

const styles = theme => ({});

class StepperExamplesMold extends AbstractStepperExamplesMold {

	constructor(props) {
		super(props);
		this.notifier = new StepperExamplesMoldNotifier(this);
		this.requester = new StepperExamplesMoldRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(withSnackbar(StepperExamplesMold));
DisplayFactory.register("StepperExamplesMold", withStyles(styles, { withTheme: true })(withSnackbar(StepperExamplesMold)));