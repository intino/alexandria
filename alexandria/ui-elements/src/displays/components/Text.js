import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractText from "../../../gen/displays/components/AbstractText";
import TextNotifier from "../../../gen/displays/notifiers/TextNotifier";
import TextRequester from "../../../gen/displays/requesters/TextRequester";

const styles = {
	label: {
		color: theme.palette.secondary.main
	},
	value: {
	}
};

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
	    const label = this.mode(this.props.label);
	    const value = this.mode(this.props.value);
		return (
			<Block layout="horizontal">
				{ label !== "" ? <div className={classes.label}>{label}</div> : null }
				<Typography variant={this.props.format} className={classes.value}>{value}</Typography>
			</Block>
		);
	};

	refresh = (value) => {
		this.setState({ "value": value });
	};

	mode = (label) => {
		if (this.props.mode === "capitalize") return this.capitalize(label);
		else if (this.props.mode === "uppercase") return label.toUpperCase();
		else if (this.props.mode === "lowercase") return label.toLowerCase();
		return label;
	};

	capitalize = (label) => {
		return label.charAt(0).toUpperCase() + label.slice(1).toLowerCase();
	};
}

export default withStyles(styles)(Text);