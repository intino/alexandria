import React from "react";
import { withStyles } from "@material-ui/core/styles";
import AbstractTextEditable from "../../../gen/displays/components/AbstractTextEditable";
import TextEditableNotifier from "../../../gen/displays/notifiers/TextEditableNotifier";
import TextEditableRequester from "../../../gen/displays/requesters/TextEditableRequester";
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

class TextEditable extends AbstractTextEditable {
	state = {
		value : "",
		readonly : this.props.readonly
	};

	constructor(props) {
		super(props);
		this.notifier = new TextEditableNotifier(this);
		this.requester = new TextEditableRequester(this);
	};

	handleChange(e) {
		this.setState({ value: e.target.value });
		Delayer.execute(this, () => this.requester.notifyChange(this.state.value), 500);
	};

	handleKeypress(e) {
		this.requester.notifyKeyPress({ keyCode: e.key, value: e.target.value });
		this.setState({ value: e.target.value });
	};

	render() {
		const { classes } = this.props;
		const label = this.props.label !== "" ? this.props.label : undefined;
		const placeholder = this.props.placeholder !== "" ? this.props.placeholder : undefined;
		const type = this.props.type != null ? this.props.type : undefined;

		return (
			<TextField format={this.variant("body1")} style={this.style()} className={classes.default} label={label} type="text"
					   value={this.state.value} onChange={this.handleChange.bind(this)} disabled={this.state.readonly}
					   onKeyPress={this.handleKeypress.bind(this)} type={type} autoFocus={this.props.focused}
					   placeholder={placeholder} multiline={this._multiline()} rows={this._rowsCount()}
					   InputProps={{
						   startAdornment: this.props.prefix !== undefined ? <InputAdornment position="start">{this.props.prefix}</InputAdornment> : undefined,
						   endAdornment: this.props.suffix !== undefined ? <InputAdornment position="end">{this.props.suffix}</InputAdornment> : undefined
					   }}></TextField>
		);
	};

	_multiline = () => {
		const mode = this.props.editionMode;
		return mode != null && mode === "Raw";
	};

	_rowsCount = () => {
		const rows = this.props.rows;
		return rows != null ? rows : undefined;
	};

	refresh = (value) => {
		this.setState({ "value": value != null ? value : "" });
	};

	refreshReadonly = (readonly) => {
		this.setState({ readonly });
	};
}

export default withStyles(styles, { withTheme: true })(withSnackbar(TextEditable));
DisplayFactory.register("TextEditable", withStyles(styles, { withTheme: true })(withSnackbar(TextEditable)));