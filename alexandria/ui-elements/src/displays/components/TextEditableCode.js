import React from "react";
//import ReactAce from 'react-ace-editor';
import { withStyles } from '@material-ui/core/styles';
import AbstractTextEditableCode from "../../../gen/displays/components/AbstractTextEditableCode";
import TextEditableCodeNotifier from "../../../gen/displays/notifiers/TextEditableCodeNotifier";
import TextEditableCodeRequester from "../../../gen/displays/requesters/TextEditableCodeRequester";

const styles = theme => ({
	editor: {
		width: "100%",
		minHeight: "100px",
		fontSize: "12pt"
	}
});

class TextEditableCode extends AbstractTextEditableCode {
	state = {
		value : this.props.children
	};

	constructor(props) {
		super(props);
		this.notifier = new TextEditableCodeNotifier(this);
		this.requester = new TextEditableCodeRequester(this);
	};

	handleChange(e) {
		if (this.timeout != null) window.clearTimeout(this.timeout);
		const value = e.target.value;
		this.setState({ value: value });
		this.timeout = window.setTimeout(() => {
			this.requester.notifyChange(value.replace(/\+/g, "&plus;"));
		}, 1000);
	};

	render() {
		const { classes } = this.props;
		return (
			<textarea className={classes.editor} value={this.state.value} onChange={this.handleChange.bind(this)}></textarea>
		);
	};

	refresh = (value) => {
		this.setState({ "value": value });
	};
}

export default withStyles(styles, { withTheme: true })(TextEditableCode);