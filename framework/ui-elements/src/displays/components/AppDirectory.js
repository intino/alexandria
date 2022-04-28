import React, { Suspense } from "react";
import { withStyles } from '@material-ui/core/styles';
import { Typography, IconButton, Popover, List, ListItem } from '@material-ui/core';
import AbstractAppDirectory from "../../../gen/displays/components/AbstractAppDirectory";
import AppDirectoryNotifier from "../../../gen/displays/notifiers/AppDirectoryNotifier";
import AppDirectoryRequester from "../../../gen/displays/requesters/AppDirectoryRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';

const styles = theme => ({
    container : {
        minWidth: '300px',
    },
    listItem : {
        opacity: '1 !important',
    },
    selected : {
        fontWeight: 'bold',
        color: 'black',
    },
});

const AppDirectoryMui = React.lazy(() => {
	return new Promise(resolve => {
		setTimeout(() => resolve(import("./icon/MuiIcon"), 300));
	});
});

class AppDirectory extends AbstractAppDirectory {

	constructor(props) {
		super(props);
		this.notifier = new AppDirectoryNotifier(this);
		this.requester = new AppDirectoryRequester(this);
		this.state = {
		    ...this.state,
		    icon: this.props.icon,
		    applications: [],
		    opened: false,
		    button: null,
		};
	};

    render() {
		if (!this.state.visible) return (<React.Fragment/>);
		const { classes, theme } = this.props;
		return (
			<Suspense fallback={<div style={{width: "24px", ...this.style()}}/>}>
			    <div style={this.style()}>
                    <IconButton color="primary" onClick={this.handleOpen.bind(this)}>
                        <AppDirectoryMui titleAccess={this.translate("Open")} icon={this.state.icon} fontSize="large"/>
                    </IconButton>
                    {this.renderPopover()}
                </div>
			</Suspense>
        );
    };

    renderPopover = () => {
        const { classes } = this.props;
        return (
            <Popover id={this.props.id} open={this.state.opened} anchorEl={this.state.button} onClose={this.handleClose.bind(this)}
                anchorOrigin={{
                    vertical: 'bottom',
                    horizontal: 'right',
                }}
                transformOrigin={{
                    vertical: 'top',
                    horizontal: 'right',
                }}>
                <div className={classes.container}>{this.renderApplications()}</div>
            </Popover>
        );
    };

	renderApplications = () => {
		const applications = this.state.applications;
        if (applications.length <= 0) return (<div style={{padding:'10px'}}>{this.translate("No applications")}</div>);
        const children = applications.map(a => { return this.renderApplication(a); });
        return (<List>{children}</List>);
    };

	renderApplication = (application) => {
	    const { classes } = this.props;
	    const className = application.selected ? classes.selected : undefined;
		return (
		    <ListItem className={classes.listItem} key={application.name} button disabled={application.selected} onClick={this.handleSelect.bind(this, application)}>
		        <Typography className={className} variant="body1">{application.name}</Typography>
            </ListItem>
        );
	};

    refresh = (info) => {
        this.setState({icon: info.icon != null ? info.icon : "Apps", applications: info.applications });
    };

    handleOpen = (e) => {
        this.setState({opened: true, button: e.currentTarget});
    };

    handleClose = (e) => {
        this.setState({opened: false, button: null});
    };

    handleSelect = (application, e) => {
        window.open(application.url, "_blank");
        e.stopPropagation();
    };

}

export default withStyles(styles, { withTheme: true })(withSnackbar(AppDirectory));
DisplayFactory.register("AppDirectory", withStyles(styles, { withTheme: true })(withSnackbar(AppDirectory)));