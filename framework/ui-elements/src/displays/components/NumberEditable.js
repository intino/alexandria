import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractNumberEditable from "../../../gen/displays/components/AbstractNumberEditable";
import NumberEditableNotifier from "../../../gen/displays/notifiers/NumberEditableNotifier";
import NumberEditableRequester from "../../../gen/displays/requesters/NumberEditableRequester";
import TextField from '@material-ui/core/TextField';
import InputAdornment from '@material-ui/core/InputAdornment';
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";
import { withSnackbar } from 'notistack';
import Delayer from '../../util/Delayer';

const styles = theme => ({
	default : {
		width: "100%"
	}
});

class NumberEditable extends AbstractNumberEditable {
	state = {
		value : "",
		readonly: this.props.readonly
	};

	constructor(props) {
		super(props);
		this.notifier = new NumberEditableNotifier(this);
		this.requester = new NumberEditableRequester(this);
	};

	handleChange(e) {
		const value = e.target.value;
		if (value === "") return;
		this.setState({ value: value });
		Delayer.execute(this, () => this.requester.notifyChange(value), 500);
	};

	render() {
		const { classes } = this.props;
		const label = this.props.label !== "" ? this.props.label : undefined;

		return (
			<TextField format={this.variant("body1")} style={this.style()} className={classes.default} label={label} type="number"
					   value={this.state.value} onChange={this.handleChange.bind(this)} disabled={this.state.readonly} autoFocus={this.props.focused}
					   inputProps={{
						   min: this.props.min !== -1 ? this.props.min : undefined,
						   max: this.props.max !== -1 ? this.props.max : undefined,
						   step: this.props.step !== -1 ? this.props.step : undefined
					   }}
					   InputProps={{
						   startAdornment: this.props.prefix !== undefined ? <InputAdornment position="start">{this.props.prefix}</InputAdornment> : undefined,
						   endAdornment: this.props.suffix !== undefined ? <InputAdornment position="end">{this.props.suffix}</InputAdornment> : undefined
					   }}/>
		);
	};

	refresh = (value) => {
		this.setState({ "value": value });
	};

	refreshReadonly = (readonly) => {
		this.setState({ readonly });
	};
}

export default withStyles(styles, { withTheme: true })(withSnackbar(NumberEditable));
DisplayFactory.register("NumberEditable", withStyles(styles, { withTheme: true })(withSnackbar(NumberEditable)));