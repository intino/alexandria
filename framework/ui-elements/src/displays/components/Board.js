import React, { Suspense } from "react";
import { withStyles } from '@material-ui/core/styles';
import { IconButton, Popover, List, ListItem } from '@material-ui/core';
import AbstractBoard from "../../../gen/displays/components/AbstractBoard";
import BoardNotifier from "../../../gen/displays/notifiers/BoardNotifier";
import BoardRequester from "../../../gen/displays/requesters/BoardRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';

const styles = theme => ({
    container : {
        minWidth: '300px',
    }
});

const BoardMui = React.lazy(() => {
	return new Promise(resolve => {
		setTimeout(() => resolve(import("./icon/MuiIcon"), 300));
	});
});

class Board extends AbstractBoard {

	constructor(props) {
		super(props);
		this.notifier = new BoardNotifier(this);
		this.requester = new BoardRequester(this);
		this.state = {
		    ...this.state,
		    icon: this.props.icon,
		    applications: [],
		    opened: false,
		    button: null,
		}
	};

    render() {
		if (!this.state.visible) return (<React.Fragment/>);
		const { classes, theme } = this.props;
		return (
			<Suspense fallback={<div style={{width: "24px", ...this.style()}}/>}>
			    <div>
                    <IconButton color="primary" style={this.style()}
                                onClick={this.handleOpen.bind(this)}>
                        <BoardMui titleAccess={this.translate("Open")} icon={this.state.icon}/>
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
                <div className={classes.container} style={this.style()}>{this.renderApplications()}</div>
            </Popover>
        );
    };

	renderApplications = () => {
		const applications = this.state.applications;
        if (applications.length <= 0) return (<div></div>);
        const children = applications.map(a => { return this.renderApplication(a); });
        return (<List>{children}</List>);
    };

	renderApplication = (application) => {
		return (
		    <ListItem key={application.name} button onClick={this.handleSelect.bind(this, application)}>
		        <div>{application.name}</div>
            </ListItem>
        );
	};

    refresh = (info) => {
        this.setState({icon: info.icon != null ? info.icon : "Apps", applications: info.applications});
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

export default withStyles(styles, { withTheme: true })(withSnackbar(Board));
DisplayFactory.register("Board", withStyles(styles, { withTheme: true })(withSnackbar(Board)));