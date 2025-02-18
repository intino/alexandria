import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractAuthenticate from "../../../gen/displays/components/AbstractAuthenticate";
import AuthenticateNotifier from "../../../gen/displays/notifiers/AuthenticateNotifier";
import AuthenticateRequester from "../../../gen/displays/requesters/AuthenticateRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';

const styles = theme => ({});

class Authenticate extends AbstractAuthenticate {

	constructor(props) {
		super(props);
		this.notifier = new AuthenticateNotifier(this);
		this.requester = new AuthenticateRequester(this);
	};

    sign = () => {
        window.crypto.subtle.signText("Hola")
    };

}

export default withStyles(styles, { withTheme: true })(withSnackbar(Authenticate));
DisplayFactory.register("Authenticate", withStyles(styles, { withTheme: true })(withSnackbar(Authenticate)));