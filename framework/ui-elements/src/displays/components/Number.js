import React from "react";
import { withStyles } from '@material-ui/core/styles';
import Typography from "@material-ui/core/Typography";
import Popover from "@material-ui/core/Popover";
import AbstractNumber from "../../../gen/displays/components/AbstractNumber";
import NumberNotifier from "../../../gen/displays/notifiers/NumberNotifier";
import NumberRequester from "../../../gen/displays/requesters/NumberRequester";
import TextBehavior from "./behaviors/TextBehavior";
import Block from "./Block";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";
import NumberUtil from "alexandria-ui-elements/src/util/NumberUtil";
import ComponentBehavior from "./behaviors/ComponentBehavior";
import classnames from "classnames";

const styles = theme => ({
	label: {
		color: theme.palette.grey.A700,
		marginRight: "5px"
	},
	value : {
		margin: "0 !important"
	},
	prefix : {
		color: theme.palette.grey.A700,
		fontSize: "10pt",
//		marginTop: "2px",
		marginRight: "3px"
	},
	suffix : {
		color: theme.palette.grey.A700,
		fontSize: "10pt",
		marginLeft: "3px"
	},
	withExpanded : {
		cursor: "pointer",
		color: theme.palette.primary.main,
	}
});

class Number extends AbstractNumber {

	constructor(props) {
		super(props);
		this.notifier = new NumberNotifier(this);
		this.requester = new NumberRequester(this);
		this.state = {
			...this.state,
		}
	};

	render() {
		const { classes } = this.props;
		const value = this.state.value;
		const variant = this.variant("body1");
		const expanded = this.state.expanded;
		const format = expanded ? this._defaultFormat() : this._format();
		const expandedClass = this.props.style != null ? classes.withExpanded : undefined;

		if (value == null || value === "" || !this.state.visible) return (<React.Fragment/>);

		return (
			<Block layout="horizontal center" style={this.style()}>
				{ ComponentBehavior.labelBlock(this.props, 'body1', {...this.style(),margin:'0 5px 0 0'}) }
				{this.state.prefix !== undefined ? <Typography variant={variant} className={classes.prefix}>{this.state.prefix}</Typography> : undefined }
				<Typography className={classnames(expandedClass, classes.value)} onClick={this.handleToggleExpanded.bind(this)} variant={variant} style={this.style()}>{NumberUtil.format(value, this.translate(format))}</Typography>
				{ this.state.suffix !== undefined ? <Typography variant={variant} className={classes.suffix}>{this.state.suffix}</Typography> : undefined }
			</Block>
		);
	};

	refresh = (value) => {
		this.setState({ value });
	};

	_format = () => {
		const style = this.props.style;
		if (style == null) return this._defaultFormat();
		const format = this._addDecimals("0");
		if (style === "Currency") return format + "a";
		else if (style === "Bytes") return format + "b";
		else if (style === "Percentage" && this.state.suffix == null) return "(" + format + "%)";
		else if (style === "Exponential" && this.state.suffix == null) return "(" + format + "e+0)";
		return this._defaultFormat();
	};

	_defaultFormat = () => {
		return this._addDecimals("0,0");
	};

	_addDecimals = (format) => {
		const decimals = this.state.decimals != null ? this.state.decimals : 0;
		if (decimals > 0) format += ".";
		for (let i=0; i<decimals; i++) format += "0";
		return format;
	};

	handleToggleExpanded = () => {
		this.setState({ expanded: !this.state.expanded });
	};

}

export default withStyles(styles, { withTheme: true })(Number);
DisplayFactory.register("Number", withStyles(styles, { withTheme: true })(Number));