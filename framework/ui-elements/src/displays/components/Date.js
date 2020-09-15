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
			value : this.props.value,
		}
	};

	render() {
		if (!this.state.visible) return (<React.Fragment/>);

		const { classes } = this.props;
		const pattern = this.props.pattern !== "" ? this.props.pattern : undefined;
		const hasMode = this.props.mode != null;

		return (
			<Block layout="horizontal center">
				{ ComponentBehavior.labelBlock(this.props, 'body1', {...this.style(),margin:'0 5px 0 0'}) }
				<Typography variant={this.variant("body1")} className={classes.value} style={this.style()}>
					{this.state.value &&
					<Moment format={!hasMode ? pattern : undefined}
							fromNow={this.props.mode === "fromnow"}
							toNow={this.props.mode === "tonow"}
							ago date={this.state.value}/>
					}
					{!this.state.value && <React.Fragment>-</React.Fragment>}
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