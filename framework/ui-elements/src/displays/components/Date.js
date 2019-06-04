import React from "react";
import Typography from '@material-ui/core/Typography';
import { withStyles } from '@material-ui/core/styles';
import Moment from 'react-moment';
import 'moment-timezone';
import AbstractDate from "../../../gen/displays/components/AbstractDate";
import DateNotifier from "../../../gen/displays/notifiers/DateNotifier";
import DateRequester from "../../../gen/displays/requesters/DateRequester";
import Block from "./Block";
import ComponentBehavior from "./behaviors/ComponentBehavior";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

const styles = theme => ({
	label: {
		color: theme.palette.grey.primary,
		marginRight: "10px"
	},
	value: {
		color: "inherit"
	}
});

class Date extends AbstractDate {
	state = {
		value : this.props.value
	};

	constructor(props) {
		super(props);
		this.notifier = new DateNotifier(this);
		this.requester = new DateRequester(this);
	};

	render() {
		const { classes } = this.props;
		const label = ComponentBehavior.label(this.props);
		const pattern = this.props.pattern !== "" ? this.props.pattern : undefined;
		const labelBlock = (label !== undefined) ? <Typography variant="body1" className={classes.label}>{label}</Typography> : undefined;
		const dateToFormat = this.state.value != null ? new Date(this.state.value) : null;
		const hasMode = this.props.mode != null;

		return (
			<Block layout="horizontal">
				{ labelBlock }
				<Typography variant={this.variant("body1")} className={classes.value}>
					<Moment format={!hasMode ? pattern : undefined}
							fromNow={this.props.mode === "fromnow"}
							toNow={this.props.mode === "tonow"}
							ago date={dateToFormat}/>
				</Typography>
			</Block>
		);
	};

	refresh = (value) => {
		this.setState({ "value": value });
	};

}

export default withStyles(styles, { withTheme: true })(Date);
DisplayFactory.register("Date", withStyles(styles, { withTheme: true })(Date));