import React from "react";
import {withStyles} from "@material-ui/core";
import AbstractTextEditable from "../../../gen/displays/components/AbstractTextEditable";
import TextEditableNotifier from "../../../gen/displays/notifiers/TextEditableNotifier";
import TextEditableRequester from "../../../gen/displays/requesters/TextEditableRequester";
import Input from '@material-ui/core/Input';

const styles = theme => ({});

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
	}

	handleKeypress(e) {
		this.requester.notifyKeyPress({ keyCode: e.key, value: e.target.value });
		this.setState({ value: e.target.value });
	}

	render() {
		const { classes } = this.props;
		const format = this.props.format !== "default" ? this.props.format : "body1";

		return (
				<Input type="text" value={this.state.value}
				   onChange={this.handleChange.bind(this)}
				   onKeyPress={this.handleKeypress.bind(this)}></Input>
		);
	};

	refresh = (value) => {
		this.setState({ "value": value });
	};
}

export default withStyles(styles, { withTheme: true })(TextEditable);