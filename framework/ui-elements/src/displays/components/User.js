import React from "react";
import { withStyles } from '@material-ui/core/styles';
import { Typography, Link } from '@material-ui/core';
import AbstractUser from "../../../gen/displays/components/AbstractUser";
import UserNotifier from "../../../gen/displays/notifiers/UserNotifier";
import UserRequester from "../../../gen/displays/requesters/UserRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';

const styles = theme => ({
	photo : {
		height: "50px",
		width: "50px",
		borderRadius: "12px",
		marginRight: "10px",
	},
	link : {
		color: theme.palette.secondary.main
	}
});

class User extends AbstractUser {
	state = {
		info: null
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
		return (
			<div className="layout horizontal">
				<img className={classes.photo} src={info.photo} title={info.fullName}/>
				<div className="layout vertical center-justified">
					<Typography variant={variant}>{info.fullName}</Typography>
					<Link className={classes.link} component="button" variant={variant} onClick={this.handleLogout.bind(this)}>{this.translate("Logout")}</Link>
				</div>
			</div>
		);
	}

	handleLogout = () => {
		this.requester.logout();
	};

	refresh = (info) => {
		this.setState({info});
	};

}

export default withStyles(styles, { withTheme: true })(withSnackbar(User));
DisplayFactory.register("User", withStyles(styles, { withTheme: true })(withSnackbar(User)));