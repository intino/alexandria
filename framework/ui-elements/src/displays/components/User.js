import React from "react";
import { withStyles } from '@material-ui/core/styles';
import { Typography, Link, Popover } from '@material-ui/core';
import IconButton from '@material-ui/core/IconButton'
import PowerSettingsNew from '@material-ui/icons/PowerSettingsNew'
import AbstractUser from "../../../gen/displays/components/AbstractUser";
import UserNotifier from "../../../gen/displays/notifiers/UserNotifier";
import UserRequester from "../../../gen/displays/requesters/UserRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';
import Divider from "./Divider";
import classnames from "classnames";
import 'alexandria-ui-elements/res/styles/layout.css';

const styles = theme => ({
    photo : {
        height: "50px",
        width: "50px",
        borderRadius: "25px",
    },
    trigger : {
        cursor: "pointer"
    },
    link : {
        color: theme.palette.secondary.main
    },
    dialogContent: {
        minWidth: "300px",
    },
    air: {
        padding: "10px",
    }
});

class User extends AbstractUser {
    state = {
        info: null,
        trigger : null
    };

    constructor(props) {
        super(props);
        this.notifier = new UserNotifier(this);
        this.requester = new UserRequester(this);
    };

    render() {
        const info = this.state.info;
        if (info == null) return (<React.Fragment/>);

        const { classes } = this.props;
        const variant = this.variant("body1");
        const photoWithFullname = this.props.mode == null || this.props.mode === "PhotoWithFullname";
        const hasChildren = this.props.children != null && this.props.children.length > 0;

        return (
            <div className="layout horizontal">
                <a ref={this.trigger} onClick={this.handleOpenDialog.bind(this)} title={info.fullName} className={!photoWithFullname || hasChildren ? classes.trigger : undefined}><img className={classes.photo} src={info.photo} title={info.fullName}/></a>
                {photoWithFullname &&
                <div style={{marginLeft:"10px"}}>
                    <div className="layout vertical center-justified hidden-ifmobile">
                        <Typography variant={variant}>{info.fullName}</Typography>
                        <Link className={classes.link} component="button" variant={variant} onClick={this.handleLogout.bind(this)}>{this.translate("Logout")}</Link>
                    </div>
                    <div className="layout vertical center-justified hidden-ifnotmobile">
                        <IconButton onClick={this.handleLogout.bind(this)} className={classes.link}><PowerSettingsNew/></IconButton>
                    </div>
                </div>
                }
                {(hasChildren || !photoWithFullname) &&
                <Popover
                    open={this.state.trigger != null}
                    anchorEl={this.state.trigger}
                    onClose={this.handleCloseDialog.bind(this)}
                    anchorOrigin={{
                        vertical: 'bottom',
                        horizontal: 'right',
                    }}
                    transformOrigin={{
                        vertical: 'top',
                        horizontal: 'right',
                    }}>
                    <div className={classes.dialogContent}>
                        {!photoWithFullname &&
                        <React.Fragment>
                            <Typography className={classes.air}>{info.fullName}</Typography>
                            <Divider/>
                        </React.Fragment>
                        }
                        {this.props.children}
                        {!photoWithFullname &&
                        <React.Fragment>
                            <Divider/>
                            <div className={classnames(classes.air, "layout horizontal end-justified")}>
                                <Link component="button" variant={variant} onClick={this.handleLogout.bind(this)}>{this.translate("Logout")}</Link>
                            </div>
                        </React.Fragment>
                        }
                    </div>
                </Popover>
                }
            </div>
        );
    }

    handleOpenDialog = (e) => {
        this.setState({trigger: e.currentTarget});
        this.requester.refreshChildren();
    };

    handleCloseDialog = () => {
        this.setState({trigger: null});
    };

    handleLogout = () => {
        this.requester.logout();
    };

    refresh = (info) => {
        this.setState({info});
    };

}

export default withStyles(styles, { withTheme: true })(withSnackbar(User));
DisplayFactory.register("User", withStyles(styles, { withTheme: true })(withSnackbar(User)));