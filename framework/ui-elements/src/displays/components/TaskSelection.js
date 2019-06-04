import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractTaskSelection from "../../../gen/displays/components/AbstractTaskSelection";
import TaskSelectionNotifier from "../../../gen/displays/notifiers/TaskSelectionNotifier";
import TaskSelectionRequester from "../../../gen/displays/requesters/TaskSelectionRequester";
import Operation from "./Operation"
import { withSnackbar } from 'notistack';
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

class TaskSelection extends AbstractTaskSelection {
	constructor(props) {
		super(props);
		this.notifier = new TaskSelectionNotifier(this);
		this.requester = new TaskSelectionRequester(this);
	};
}

export default withStyles(Operation.Styles, { withTheme: true })(withSnackbar(TaskSelection));
DisplayFactory.register("TaskSelection", withStyles(Operation.Styles, { withTheme: true })(withSnackbar(TaskSelection)));