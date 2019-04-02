import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractNumberEditable from "../../../gen/displays/components/AbstractNumberEditable";
import NumberEditableNotifier from "../../../gen/displays/notifiers/NumberEditableNotifier";
import NumberEditableRequester from "../../../gen/displays/requesters/NumberEditableRequester";
import TextField from '@material-ui/core/TextField';
import InputAdornment from '@material-ui/core/InputAdornment';

const styles = theme => ({
	default : {
		width: "100%"
	}
});

class NumberEditable extends AbstractNumberEditable {
	state = {
		value : ""
	};

	constructor(props) {
		super(props);
		this.notifier = new NumberEditableNotifier(this);
		this.requester = new NumberEditableRequester(this);
	};

	handleChange(e) {
		const value = e.target.value;
		if (value === "") return;
		this.requester.notifyChange(value);
		this.setState({ value: value });
	};

	render() {
		const { classes } = this.props;
		const value = this.state.value !== "" ? this.state.value : (this.props.min !== -1 ? this.props.min : 0);
		const label = this.props.label !== "" ? this.props.label : undefined;
		const format = this.props.format != null && this.props.format !== "default" ? this.props.format.split(" ")[0] : "body1";

		return (
			<TextField format={format} style={this.style()} className={classes.default} label={label} type="number"
					   value={value} onChange={this.handleChange.bind(this)}
					   inputProps={{
						   min: this.props.min !== -1 ? this.props.min : undefined,
						   max: this.props.max !== -1 ? this.props.max : undefined,
						   step: this.props.step !== -1 ? this.props.step : undefined
					   }}
					   InputProps={{
						   startAdornment: this.props.prefix !== undefined ? <InputAdornment position="start">{this.props.prefix}</InputAdornment> : undefined,
						   endAdornment: this.props.suffix !== undefined ? <InputAdornment position="end">{this.props.suffix}</InputAdornment> : undefined
					   }}></TextField>
		);
	};

	refresh = (value) => {
		this.setState({ "value": value });
	};

}

export default withStyles(styles, { withTheme: true })(NumberEditable);