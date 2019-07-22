import React from "react";
import { withStyles } from '@material-ui/core/styles';
import { IconButton, TextField, Dialog, DialogTitle, DialogContent, DialogActions, Button } from "@material-ui/core";
import Settings from "@material-ui/icons/Settings";
import AbstractDashboard from "../../../gen/displays/components/AbstractDashboard";
import DashboardNotifier from "../../../gen/displays/notifiers/DashboardNotifier";
import DashboardRequester from "../../../gen/displays/requesters/DashboardRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { Typography } from "@material-ui/core";
import Spinner from "./Spinner";
import classNames from "classnames";
import { withSnackbar } from 'notistack';

const styles = theme => ({
	container : {
		width: "100%",
		height: "100%",
		border: "0",
        position: "relative",
	},
	error: {
		color: theme.palette.error.main,
		margin: "10px 0"
	},
    settings : {
	    position: "absolute",
	    zIndex: 1,
		background: "white",
		border: "1px solid #ddd",
		width: "100%",
    },
	spaced : {
		marginBottom: "30px",
	},
});

class Dashboard extends AbstractDashboard {
	state = {
		loading : false,
		error : undefined,
		location: undefined,
        driverDefined: undefined,
		adminMode: false,
		settingsOpened: false,
		serverScript: null,
		uiScript: null,
	};

	constructor(props) {
		super(props);
		this.notifier = new DashboardNotifier(this);
		this.requester = new DashboardRequester(this);
		this.dialog = React.createRef();
	};

	render() {
		const { classes } = this.props;
		const error = this.state.error;
		const location = this.state.location;
		const driverDefined = this.state.driverDefined;
		const frameStyle = this.state.adminMode ? { paddingTop: "48px" } : undefined;

		if (error !== undefined)
			return (<Typography style={this.style()} className={classes.error}>{error}</Typography>);

		return (
			<div className={classes.container} style={this.style()} ref={this.container}>
				{this.state.adminMode && <div className={classNames("layout horizontal end-justified", classes.settings)}><IconButton onClick={this.handleShowSettings.bind(this)}><Settings/></IconButton></div>}
				{this.state.loading ? <div className="layout horizontal center-center" style={ {margin: "10px", height: "100%"} }><Spinner/></div> : undefined}
				{(!this.state.loading && location != null) && <iframe className={classes.container} style={frameStyle} src={this.state.location}></iframe>}
				{(!this.state.loading && !driverDefined) && <div>{this.translate("No driver defined to load dashboard")}</div>}
				{(!this.state.loading && location == null) && <div>{this.translate("No dashboard script defined")}</div>}
				<Dialog ref={this.dialog} open={this.state.settingsOpened} fullWidth={true} maxWidth="500">
					<DialogTitle>{this.translate("Edit dashboard")}</DialogTitle>
					<DialogContent>
						<TextField multiline variant="outlined" rows="10" fullWidth className={classes.spaced}
							   	   label={this.translate("Server script")} value={this.state.serverScript}
								   onChange={this.handleServerScriptChange.bind(this)}></TextField>
						<TextField multiline variant="outlined" rows="10" fullWidth
							   	   label={this.translate("UI script")} value={this.state.uiScript}
								   onChange={this.handleUiScriptChange.bind(this)}></TextField>
					</DialogContent>
					<DialogActions>
						<Button onClick={this.handleClose.bind(this)} color="primary">{this.translate("Close")}</Button>
					</DialogActions>
				</Dialog>
			</div>
		);
	};

	handleShowSettings = () => {
		this.requester.settings();
	};

	handleServerScriptChange = (e) => {
		this.setState({ "serverScript" : e.target.value});
		this.requester.saveServerScript(e.target.value);
	};

	handleUiScriptChange = (e) => {
		this.setState({ "uiScript" : e.target.value});
		this.requester.saveUiScript(e.target.value);
	}

	showLoading = () => {
		this.setState({ "loading" : true, "error" : undefined });
	};

	refresh = (info) => {
		this.setState({ "location": info.location, "driverDefined": info.driverDefined,
                        "serverScript": info.serverScript, "uiScript": info.uiScript,
                        "adminMode": info.adminMode, "loading": false, "error": undefined });
	};

	refreshError = (error) => {
		this.setState({ "error": error });
	};

	showSettings = (info) => {
		this.setState({ "settingsOpened": true, "serverScript": info.serverScript, "uiScript": info.uiScript });
	};

	handleClose = () => {
		this.setState({ "settingsOpened": false });
	};
}

export default withStyles(styles, { withTheme: true })(withSnackbar(Dashboard));
DisplayFactory.register("Dashboard", withStyles(styles, { withTheme: true })(withSnackbar(Dashboard)));