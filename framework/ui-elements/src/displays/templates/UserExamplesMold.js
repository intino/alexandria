import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractUserExamplesMold from "../../../gen/displays/templates/AbstractUserExamplesMold";
import UserExamplesMoldNotifier from "../../../gen/displays/notifiers/UserExamplesMoldNotifier";
import UserExamplesMoldRequester from "../../../gen/displays/requesters/UserExamplesMoldRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';

const styles = theme => ({});

class UserExamplesMold extends AbstractUserExamplesMold {

	constructor(props) {
		super(props);
		this.notifier = new UserExamplesMoldNotifier(this);
		this.requester = new UserExamplesMoldRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(withSnackbar(UserExamplesMold));
DisplayFactory.register("UserExamplesMold", withStyles(styles, { withTheme: true })(withSnackbar(UserExamplesMold)));