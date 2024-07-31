import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractSetLightMode from "../../../gen/displays/components/AbstractSetLightMode";
import SetLightModeNotifier from "../../../gen/displays/notifiers/SetLightModeNotifier";
import SetLightModeRequester from "../../../gen/displays/requesters/SetLightModeRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';
import Theme from '../../../gen/Theme';
import Actionable from "./Actionable";

class SetLightMode extends AbstractSetLightMode {

	constructor(props) {
		super(props);
		const theme = Theme.get();
		this.notifier = new SetLightModeNotifier(this);
		this.requester = new SetLightModeRequester(this);
		this.state = {
            ...this.state,
            visible: this.state.appMode == "dark",
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
	    Theme.get().setMode("light");
	    this._saveAppModeInCookies("light");
	    this.setState({appMode: mode});
	};
}

export default withStyles(Actionable.Styles, { withTheme: true })(withSnackbar(SetLightMode));
DisplayFactory.register("SetLightMode", withStyles(Actionable.Styles, { withTheme: true })(withSnackbar(SetLightMode)));