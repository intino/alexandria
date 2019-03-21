import React from "react";
import Typography from '@material-ui/core/Typography';
import { withStyles } from '@material-ui/core/styles';
import Block from "./Block";
import AbstractText from "../../../gen/displays/components/AbstractText";
import TextNotifier from "../../../gen/displays/notifiers/TextNotifier";
import TextRequester from "../../../gen/displays/requesters/TextRequester";
import TextBehavior from "./behaviors/TextBehavior";

const styles = theme => ({
	label: {
		color: theme.palette.grey.primary,
		marginRight: "10px"
	},
	value: {
		color: "inherit"
	}
});

class Text extends AbstractText {
	state = {
		value : this.props.value
	};

	constructor(props) {
		super(props);
		this.notifier = new TextNotifier(this);
		this.requester = new TextRequester(this);
	};

	render() {
		const { classes } = this.props;
	    const value = TextBehavior.mode(this.state.value, this.props);
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
		this.setState({ "value": value });
	};

}

export default withStyles(styles, { withTheme: true })(Text);