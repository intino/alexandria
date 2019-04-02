import React from "react";
import {withStyles} from "@material-ui/core";
import AbstractTextEditable from "../../../gen/displays/components/AbstractTextEditable";
import TextEditableNotifier from "../../../gen/displays/notifiers/TextEditableNotifier";
import TextEditableRequester from "../../../gen/displays/requesters/TextEditableRequester";
import TextField from '@material-ui/core/TextField';
import InputAdornment from '@material-ui/core/InputAdornment';

const styles = theme => ({
	default : {
		width: "100%"
	}
});

class TextEditable extends AbstractTextEditable {
	state = {
		value : ""
	};

	constructor(props) {
		super(props);
		this.notifier = new TextEditableNotifier(this);
		this.requester = new TextEditableRequester(this);
	};

	handleChange(e) {
		this.requester.notifyChange(e.target.value);
		this.setState({ value: e.target.value });
	};

	handleKeypress(e) {
		this.requester.notifyKeyPress({ keyCode: e.key, value: e.target.value });
		this.setState({ value: e.target.value });
	};

	render() {
		const { classes } = this.props;
		const label = this.props.label !== "" ? this.props.label : undefined;
		const format = this.props.format != null && this.props.format !== "default" ? this.props.format.split(" ")[0] : "body1";

		return (
				<TextField format={format} style={this.style()} className={classes.default} label={label} type="text"
						   value={this.state.value} onChange={this.handleChange.bind(this)}
				   		   onKeyPress={this.handleKeypress.bind(this)}
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

export default withStyles(styles, { withTheme: true })(TextEditable);