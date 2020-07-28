import React from "react";
import { Typography, Spinner } from '@material-ui/core';
import { withStyles } from '@material-ui/core/styles';
import AbstractDashboardMetabase from "../../../gen/displays/components/AbstractDashboardMetabase";
import DashboardMetabaseNotifier from "../../../gen/displays/notifiers/DashboardMetabaseNotifier";
import DashboardMetabaseRequester from "../../../gen/displays/requesters/DashboardMetabaseRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
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
		background: "white",
		border: "1px solid #ddd",
		width: "100%",
	},
	spaced : {
		marginBottom: "30px",
	},
});

class DashboardMetabase extends AbstractDashboardMetabase {

	constructor(props) {
		super(props);
		this.notifier = new DashboardMetabaseNotifier(this);
		this.requester = new DashboardMetabaseRequester(this);
        this.state = {
            ...this.state,
            loading : false,
            error : undefined,
            location: undefined,
            adminMode: false,
        };
	};

    render() {
		const { classes } = this.props;
		const error = this.state.error;
		const location = this.state.location;
		const frameStyle = this.state.adminMode ? { height: "calc(100% - 48px)" } : undefined;

		if (error !== undefined)
			return (<div className="layout horizontal center-center" style={ {margin: "10px", height: "100%"} }><Typography style={this.style()} className={classes.error}>{error}</Typography></div>);

        return (
			<div className={classes.container} style={this.style()} ref={this.container}>
				{this.state.loading ? <div className="layout horizontal center-center" style={ {margin: "10px", height: "100%"} }><Spinner/></div> : undefined}
				{(!this.state.loading && location != null) && <iframe className={classes.container} style={frameStyle} src={location}/>}
            </div>
        );
    }

	showLoading = () => {
		this.setState({ "loading" : true, "error" : undefined });
	};

    refresh = (info) => {
        this.setState({ location: info.location, loading: false, error: undefined });
    };

	refreshError = (error) => {
		this.setState({ "error": error });
	};
}

export default withStyles(styles, { withTheme: true })(withSnackbar(DashboardMetabase));
DisplayFactory.register("DashboardMetabase", withStyles(styles, { withTheme: true })(withSnackbar(DashboardMetabase)));