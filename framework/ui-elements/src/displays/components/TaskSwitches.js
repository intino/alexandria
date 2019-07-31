import React from "react";
import { withStyles } from '@material-ui/core/styles';
import { Switch, FormControlLabel } from '@material-ui/core';
import AbstractTaskSwitches from "../../../gen/displays/components/AbstractTaskSwitches";
import TaskSwitchesNotifier from "../../../gen/displays/notifiers/TaskSwitchesNotifier";
import TaskSwitchesRequester from "../../../gen/displays/requesters/TaskSwitchesRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';

const styles = theme => ({});

class TaskSwitches extends AbstractTaskSwitches {

	constructor(props) {
		super(props);
		this.notifier = new TaskSwitchesNotifier(this);
		this.requester = new TaskSwitchesRequester(this);
		this.state.checked = this.props.state === "On";
	};

	renderTrigger = () => {
		const {classes} = this.props;
		return (
			<FormControlLabel
				control={<Switch checked={this.state.checked} onChange={this.handleChange.bind(this)}/>}
				label={this._title()}
			/>
		);
	};

	refreshState = (value) => {
		this.setState({ checked: value === "On"});
	};

	handleChange = () => {
		this.requester.toggle();
	};
}

export default withStyles(styles, { withTheme: true })(withSnackbar(TaskSwitches));
DisplayFactory.register("TaskSwitches", withStyles(styles, { withTheme: true })(withSnackbar(TaskSwitches)));