import React, {Suspense} from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractDashboard from "../../../gen/displays/components/AbstractDashboard";
import DashboardNotifier from "../../../gen/displays/notifiers/DashboardNotifier";
import DashboardRequester from "../../../gen/displays/requesters/DashboardRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { Typography } from "@material-ui/core";
import Spinner from "./Spinner";

const styles = theme => ({
	container : {
		width: "100%",
		height: "100%",
		border: "0",
	},
	error: {
		color: theme.palette.error.main,
		margin: "10px 0"
	}
});

class Dashboard extends AbstractDashboard {
	state = {
		loading : false,
		error : undefined,
		location: undefined
	};

	constructor(props) {
		super(props);
		this.notifier = new DashboardNotifier(this);
		this.requester = new DashboardRequester(this);
	};

	render() {
		const { classes } = this.props;
		const error = this.state.error;
		const location = this.state.location;

		if (error !== undefined)
			return (<Typography style={this.style()} className={classes.error}>{error}</Typography>);

		return (
			<div style={this.style()} ref={this.container}>
				{this.state.loading ? <div className="layout horizontal center-center" style={ {margin: "10px", height: "100%"} }><Spinner/></div> : undefined}
				{(!this.state.loading && location != null) && <iframe className={classes.container} src={this.state.location}></iframe>}
				{(!this.state.loading && location == null) && <div>{this.translate("No dashboard script defined")}</div>}
			</div>
		);
	};

	showLoading = () => {
		this.setState({ "loading" : true, "error" : undefined });
	};

	refresh = (info) => {
		this.setState({ location: info.location, "loading": false, "error": undefined });
	};

	refreshError = (error) => {
		this.setState({ "error": error });
	};
}

export default withStyles(styles, { withTheme: true })(Dashboard);
DisplayFactory.register("Dashboard", withStyles(styles, { withTheme: true })(Dashboard));