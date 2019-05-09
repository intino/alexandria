import React from "react";
import { withStyles } from '@material-ui/core/styles';
import Typography from "@material-ui/core/Typography";
import AbstractNumber from "../../../gen/displays/components/AbstractNumber";
import NumberNotifier from "../../../gen/displays/notifiers/NumberNotifier";
import NumberRequester from "../../../gen/displays/requesters/NumberRequester";
import TextBehavior from "./behaviors/TextBehavior";
import Block from "./Block";

const styles = theme => ({
	prefix : {
		color: theme.palette.grey.primary,
		fontSize: "10pt",
		marginTop: "2px",
		marginRight: "3px"
	},
	suffix : {
		color: theme.palette.grey.primary,
		fontSize: "10pt",
		marginTop: "2px",
		marginLeft: "3px"
	}
});

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
		const variant = this.variant("body1");
		const label = TextBehavior.label(this.props);
		const labelBlock = (label !== undefined) ? <Typography variant={variant} className={classes.label}>{label}</Typography> : undefined;

		if (value == null || value === "") return (<React.Fragment/>);

		return (
			<Block layout="horizontal">
				{ labelBlock }
				{this.props.prefix !== undefined ? <Typography variant={variant} className={classes.prefix}>{this.props.prefix}:</Typography> : undefined }
				<Typography variant={variant} className={classes.value} style={this.style()}>{value}</Typography>
				{ this.props.suffix !== undefined ? <Typography variant={variant} className={classes.suffix}>{this.props.suffix}</Typography> : undefined }
			</Block>
		);
	};

	refresh = (value) => {
		this.setState({ value });
	};
}

export default withStyles(styles, { withTheme: true })(Number);