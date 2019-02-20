import React from "react";
import Typography from '@material-ui/core/Typography';
import { withStyles } from '@material-ui/core/styles';
import Block from "./Block";
import AbstractText from "../../../gen/displays/components/AbstractText";
import TextNotifier from "../../../gen/displays/notifiers/TextNotifier";
import TextRequester from "../../../gen/displays/requesters/TextRequester";

const styles = theme => ({
	label: {
		color: theme.palette.primary.main
	},
	value: {
		color: theme.palette.text.main
	}
});

class Text extends AbstractText {
	state = {
		value : ""
	};

	constructor(props) {
		super(props);
		this.notifier = new TextNotifier(this);
		this.requester = new TextRequester(this);
	};

	render() {
		const { classes } = this.props;
	    const value = this.mode(this.state.value);
	    const format = this.props.format !== "default" ? this.props.format : "inherit";
		return (
			<Block layout="horizontal">
				{ this.props.label !== "" ? <div className={classes.label}>{this.props.label}</div> : null }
				<Typography variant={format} className={classes.value}>{value}</Typography>
			</Block>
		);
	};

	refresh = (value) => {
		this.setState({ "value": value });
	};

	mode = (value) => {
		if (value == null) return value;
		if (this.props.mode === "capitalize") return this.capitalize(value);
		else if (this.props.mode === "uppercase") return value.toUpperCase();
		else if (this.props.mode === "lowercase") return value.toLowerCase();
		return value;
	};

	capitalize = (label) => {
		return label.charAt(0).toUpperCase() + label.slice(1).toLowerCase();
	};
}

export default withStyles(styles)(Text);