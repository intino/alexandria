import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractTask from "../../../gen/displays/components/AbstractTask";
import TaskNotifier from "../../../gen/displays/notifiers/TaskNotifier";
import TaskRequester from "../../../gen/displays/requesters/TaskRequester";

const styles = theme => ({});

class Task extends AbstractTask {

	constructor(props) {
		super(props);
		this.notifier = new TaskNotifier(this);
		this.requester = new TaskRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(Task);