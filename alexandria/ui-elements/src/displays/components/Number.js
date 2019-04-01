import React from "react";
import { withStyles } from '@material-ui/core/styles';
import Typography from "@material-ui/core/Typography";
import AbstractNumber from "../../../gen/displays/components/AbstractNumber";
import NumberNotifier from "../../../gen/displays/notifiers/NumberNotifier";
import NumberRequester from "../../../gen/displays/requesters/NumberRequester";
import TextBehavior from "./behaviors/TextBehavior";
import Block from "./Block";

const styles = theme => ({});

class Number extends AbstractNumber {
	state = {
		value: this.props.value
	};

	constructor(props) {
		super(props);
		this.notifier = new NumberNotifier(this);
		this.requester = new NumberRequester(this);
	};

	render() {
		const { classes } = this.props;
		const value = this.state.value;
		const format = this.props.format != null && this.props.format !== "default" ? this.props.format.split(" ")[0] : "body1";
		const label = TextBehavior.label(this.props);
		const labelBlock = (label !== undefined) ? <Typography variant={format} className={classes.label}>{label}</Typography> : undefined;

		if (value == null || value === "") return (<React.Fragment/>);

		return (
			<Block layout="horizontal">
				{ labelBlock }
				<Typography variant={format} className={classes.value} style={this.style()}>{value}</Typography>
			</Block>
		);
	};

	refresh = (value) => {
		this.setState({ value });
	};
}

export default withStyles(styles, { withTheme: true })(Number);