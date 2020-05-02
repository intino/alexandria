import React from "react";
import { withStyles } from '@material-ui/core/styles';
import { Switch, FormControlLabel } from '@material-ui/core';
import AbstractActionSwitch from "../../../gen/displays/components/AbstractActionSwitch";
import ActionSwitchNotifier from "../../../gen/displays/notifiers/ActionSwitchNotifier";
import ActionSwitchRequester from "../../../gen/displays/requesters/ActionSwitchRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';

const styles = theme => ({});

class ActionSwitch extends AbstractActionSwitch {

	constructor(props) {
		super(props);
		this.notifier = new ActionSwitchNotifier(this);
		this.requester = new ActionSwitchRequester(this);
		this.state = {
			...this.state,
			checked : this.traceValue() != null ? this.traceValue() : this.props.state === "On",
			readonly : this.props.readonly
		};
	};

	renderTrigger = () => {
		if (!this.state.visible) return (<React.Fragment/>);
		return (
			<FormControlLabel style={this.style()}
							  control={<Switch size={this._size()} disabled={this.state.readonly}
							                   checked={this.state.checked} onChange={this.handleChange.bind(this)}/>}
							  label={this._title()}
			/>
		);
	};

	refreshState = (value) => {
		this.setState({ checked: value === "On"});
	};

	handleChange = () => {
		this.requester.toggle();
		this.trace(!this.state.checked);
	};
}

export default withStyles(styles, { withTheme: true })(withSnackbar(ActionSwitch));
DisplayFactory.register("ActionSwitch", withStyles(styles, { withTheme: true })(withSnackbar(ActionSwitch)));