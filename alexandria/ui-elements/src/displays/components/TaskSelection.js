import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractTaskSelection from "../../../gen/displays/components/AbstractTaskSelection";
import TaskSelectionNotifier from "../../../gen/displays/notifiers/TaskSelectionNotifier";
import TaskSelectionRequester from "../../../gen/displays/requesters/TaskSelectionRequester";

const styles = theme => ({});

class TaskSelection extends AbstractTaskSelection {

	constructor(props) {
		super(props);
		this.notifier = new TaskSelectionNotifier(this);
		this.requester = new TaskSelectionRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(TaskSelection);