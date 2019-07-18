import React from "react";
import Typography from '@material-ui/core/Typography';
import { withStyles } from '@material-ui/core/styles';
import Block from "./Block";
import AbstractText from "../../../gen/displays/components/AbstractText";
import TextNotifier from "../../../gen/displays/notifiers/TextNotifier";
import TextRequester from "../../../gen/displays/requesters/TextRequester";
import TextBehavior from "./behaviors/TextBehavior";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

const styles = theme => ({
	label: {
		color: theme.palette.grey.primary,
		marginRight: "5px"
	},
	value: {
		color: 'inherit',
	},
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

	style() {
		var result = super.style();
		if (this.props.color != null) result.color = this.props.color;
		if (!this.props.highlighted) return result;
		result.color = this.props.highlighted.text;
		result.background = this.props.highlighted.background;
		result.padding = "0 10px";
		result.borderRadius = "3px";
		return result;
	};

	refresh = (value) => {
		this.setState({ "value": value });
	};

}

export default withStyles(styles, { withTheme: true })(Text);
DisplayFactory.register("Text", withStyles(styles, { withTheme: true })(Text));