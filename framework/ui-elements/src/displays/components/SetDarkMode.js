import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractSetDarkMode from "../../../gen/displays/components/AbstractSetDarkMode";
import SetDarkModeNotifier from "../../../gen/displays/notifiers/SetDarkModeNotifier";
import SetDarkModeRequester from "../../../gen/displays/requesters/SetDarkModeRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';
import Theme from 'app-elements/gen/Theme';
import Actionable from "./Actionable";

class SetDarkMode extends AbstractSetDarkMode {

	constructor(props) {
		super(props);
		const theme = Theme.get();
		this.notifier = new SetDarkModeNotifier(this);
		this.requester = new SetDarkModeRequester(this);
		this.state = {
            ...this.state,
            visible: this.state.appMode == "light"
		};
	};

	render() {
	    return (
	        <React.Fragment>
	            {this.renderCookieConsent()}
	            {super.render()}
	        </React.Fragment>
	    );
	};

	updateMode = () => {
	    Theme.get().setMode("dark");
	    this._saveAppModeInCookies("dark");
	    this.setState({appMode: mode});
	};

}

export default withStyles(Actionable.Styles, { withTheme: true })(withSnackbar(SetDarkMode));
DisplayFactory.register("SetDarkMode", withStyles(Actionable.Styles, { withTheme: true })(withSnackbar(SetDarkMode)));