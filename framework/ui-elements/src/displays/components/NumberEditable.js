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

	constructor(props) {
		super(props);
		this.notifier = new NumberEditableNotifier(this);
		this.requester = new NumberEditableRequester(this);
        this.state = {
            ...this.state,
            readonly: this.props.readonly,
        };
	};

	handleChange(e) {
		const value = e.target.value;
		this.setState({ value: value });
		Delayer.execute(this, () => this.requester.notifyChange(value !== "" ? value : "0"), 500);
	};

	render() {
		const { classes } = this.props;
		const label = this.props.label !== "" ? this.props.label : undefined;
		const error = this.state.error;
		const value = this.state.value != null ? this.state.value : (this.props.min !== -1 ? this.props.min : 0);

		return (
			<TextField format={this.variant("body1")} style={this.style()} className={classes.default} label={label} type="number"
					   value={this.state.value} onChange={this.handleChange.bind(this)} /*disabled={this.state.readonly}*/ autoFocus={this.props.focused}
					   error={error != null} helperText={this.state.readonly ? undefined : (error != null ? error : this.props.helperText)} autoComplete="off"
					   inputProps={{
						   min: this.props.min !== -1 ? this.props.min : undefined,
						   max: this.props.max !== -1 ? this.props.max : undefined,
						   step: this.props.step !== -1 ? this.props.step : undefined
					   }}
					   InputProps={{
					       readOnly: this.state.readonly,
						   startAdornment: this.state.prefix !== undefined ? <InputAdornment position="start">{this.state.prefix}</InputAdornment> : undefined,
						   endAdornment: this.state.suffix !== undefined ? <InputAdornment position="end">{this.state.suffix}</InputAdornment> : undefined
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