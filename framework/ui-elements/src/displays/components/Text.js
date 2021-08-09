import React from "react";
import Typography from '@material-ui/core/Typography';
import Tooltip from '@material-ui/core/Tooltip';
import { withStyles } from '@material-ui/core/styles';
import Block from "./Block";
import AbstractText from "../../../gen/displays/components/AbstractText";
import TextNotifier from "../../../gen/displays/notifiers/TextNotifier";
import TextRequester from "../../../gen/displays/requesters/TextRequester";
import TextBehavior from "./behaviors/TextBehavior";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";
import ComponentBehavior from "./behaviors/ComponentBehavior";

const styles = theme => ({
	label: {
		color: theme.palette.grey.A700,
		marginRight: "5px"
	},
	value: {
		color: 'inherit',
	},
	prefix : {
		color: theme.palette.grey.A700,
		marginTop: "2px",
		marginRight: "3px"
	},
	suffix : {
		color: theme.palette.grey.A700,
		marginTop: "2px",
		marginLeft: "3px"
	}
});

class Text extends AbstractText {

	constructor(props) {
		super(props);
		this.notifier = new TextNotifier(this);
		this.requester = new TextRequester(this);
		this.state = {
			...this.state,
		}
	};

	render() {
		if (!this.state.visible) return (<React.Fragment/>);

		const { classes } = this.props;
		const variant = this.variant("body1");

		let value = this.props.translate ? this.translate(this.state.value) : this.state.value;
		value = TextBehavior.mode(value, this.props);
		if (value == null || value === "") return (<React.Fragment/>);
		return (
			<Block layout="horizontal center">
				{ ComponentBehavior.labelBlock(this.props, 'body1', {...this.style(),margin:'0 5px 0 0'}) }
				{this.props.prefix !== undefined ? <Typography variant={variant} className={classes.prefix}>{this.props.prefix}:</Typography> : undefined }
                {this._requireEllipsis(value) ?
                    <Tooltip title={this.state.title} placement="top">
                        <Typography variant={variant} className={classes.value} style={this.style()}>{value}</Typography>
                    </Tooltip> :
                    <Typography variant={variant} className={classes.value} style={this.style()}>{value}</Typography>
                }
				{this.props.suffix !== undefined ? <Typography variant="caption" className={classes.suffix}>{this.props.suffix}</Typography> : undefined}
			</Block>
		);
	};

	style() {
		var result = super.style();
		if (this.props.color != null) result.color = this.props.color;
		if (this.state.color != null) result.color = this.state.color;
		if (!this.state.highlighted) return result;
		result.color = this.state.highlighted.text;
		result.background = this.state.highlighted.background;
		if (result.padding == null) result.padding = "0 10px";
		if (result.borderRadius == null) result.borderRadius = "3px";
		return result;
	};

}

export default withStyles(styles, { withTheme: true })(Text);
DisplayFactory.register("Text", withStyles(styles, { withTheme: true })(Text));