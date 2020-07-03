import React from "react";
import { withStyles } from '@material-ui/core/styles';
import ToggleButton from '@material-ui/lab/ToggleButton';
import ToggleButtonGroup from '@material-ui/lab/ToggleButtonGroup';
import AbstractActionToggle from "../../../gen/displays/components/AbstractActionToggle";
import ActionToggleNotifier from "../../../gen/displays/notifiers/ActionToggleNotifier";
import ActionToggleRequester from "../../../gen/displays/requesters/ActionToggleRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';

const styles = theme => ({});

class ActionToggle extends AbstractActionToggle {

	constructor(props) {
		super(props);
		this.notifier = new ActionToggleNotifier(this);
		this.requester = new ActionToggleRequester(this);
		this.state = {
			...this.state,
			selected : this.traceValue() != null ? this.traceValue() : this.props.state === "On",
			readonly : this.props.readonly
		};
	};

	renderTrigger = () => {
		if (!this.state.visible) return (<React.Fragment/>);
		return (
		    <ToggleButtonGroup size={this._size()} onChange={this.handleChange.bind(this)}>
                <ToggleButton id={this.props.id} style={this.style()} value={this.props.id}
                              selected={this.state.selected} disabled={this._readonly()}>
                              {this.renderContent()}
                </ToggleButton>
            </ToggleButtonGroup>
		);
	};

	refreshState = (value) => {
		this.setState({ selected: value === "On"});
	};

	handleChange = () => {
		this.requester.toggle();
		this.trace(!this.state.selected);
	};
}

export default withStyles(styles, { withTheme: true })(withSnackbar(ActionToggle));
DisplayFactory.register("ActionToggle", withStyles(styles, { withTheme: true })(withSnackbar(ActionToggle)));