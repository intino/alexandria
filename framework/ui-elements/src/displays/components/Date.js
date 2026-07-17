import React from "react";
import Typography from '@mui/material/Typography';
import {withStyles} from 'alexandria-ui-elements/src/util/muiStylesCompat';
import moment from 'moment';
import AbstractDate from "../../../gen/displays/components/AbstractDate";
import DateNotifier from "../../../gen/displays/notifiers/DateNotifier";
import DateRequester from "../../../gen/displays/requesters/DateRequester";
import Block from "./Block";
import ComponentBehavior from "./behaviors/ComponentBehavior";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

const styles = theme => ({
	label: {
		color: theme.palette.grey.A700,
		marginRight: "5px"
	},
	value: {
		color: "inherit"
	}
});

class Date extends AbstractDate {

	constructor(props) {
		super(props);
		this.notifier = new DateNotifier(this);
		this.requester = new DateRequester(this);
		this.state = {
			...this.state,
			pattern : this.props.pattern !== "" ? this.props.pattern : undefined,
			value : this.props.value,
		}
	};

	render() {
		if (!this.state.visible) return (<React.Fragment/>);

		const { classes } = this.props;
		const pattern = this.state.pattern;
		const hasMode = this.props.mode != null;
		const language = window.Application.configuration.language;

		return (
			<Block layout="horizontal center">
				{ ComponentBehavior.labelBlock(this.props, 'body1', {...this.style(),margin:'0 5px 0 0'}) }
				<Typography variant={this.variant("body1")} className={classes.value} style={this.style()}>
					{this.state.value && this.renderValue(pattern, hasMode, language)}
					{!this.state.value && <React.Fragment>-</React.Fragment>}
				</Typography>
			</Block>
		);
	};

	renderValue = (pattern, hasMode, language) => {
		const value = this.props.useTimezone ? moment(this.state.value) : moment.utc(this.state.value);
		if (this.props.mode === "fromnow") return value.locale(language).fromNow();
		if (this.props.mode === "tonow") return value.locale(language).toNow();
		if (this.props.mode === "ago") return value.locale(language).fromNow();
		return value.locale(language).format(!hasMode ? pattern : undefined);
	};

	refresh = (value) => {
		this.setState({ "value": value });
	};

	refreshPattern = (pattern) => {
	    this.setState({ pattern });
	};

}

export default withStyles(styles, { withTheme: true })(Date);
DisplayFactory.register("Date", withStyles(styles, { withTheme: true })(Date));
