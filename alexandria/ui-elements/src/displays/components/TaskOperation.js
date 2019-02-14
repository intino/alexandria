import React from "react";
import AbstractTaskOperation from "../../../gen/displays/components/AbstractTaskOperation";
import TaskOperationNotifier from "../../../gen/displays/notifiers/TaskOperationNotifier";
import TaskOperationRequester from "../../../gen/displays/requesters/TaskOperationRequester";

export default class TaskOperation extends AbstractTaskOperation {

	constructor(props) {
		super(props);
		this.notifier = new TaskOperationNotifier(this);
		this.requester = new TaskOperationRequester(this);
	};

	render() {
		return (
			<React.Fragment>
			</React.Fragment>
		);
	};


}