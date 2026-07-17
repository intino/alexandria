import React from "react";
import {withStyles} from 'alexandria-ui-elements/src/util/muiStylesCompat';
import AbstractSetLightMode from "../../../gen/displays/components/AbstractSetLightMode";
import SetLightModeNotifier from "../../../gen/displays/notifiers/SetLightModeNotifier";
import SetLightModeRequester from "../../../gen/displays/requesters/SetLightModeRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import {withSnackbar} from "alexandria-ui-elements/src/util/notistackCompat";
import Theme from 'app-elements/gen/Theme';
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

	renderActionable() {
	    return (
	        <React.Fragment>
	            {this.renderCookieConsent()}
	            {super.renderActionable()}
	        </React.Fragment>
	    );
	};

	updateMode = () => {
	    Theme.get().setMode("light");
	    this._saveAppModeInCookies("light");
	    this.setState({appMode: "light"});
	};
}

export default withStyles(Actionable.Styles, { withTheme: true })(withSnackbar(SetLightMode));
DisplayFactory.register("SetLightMode", withStyles(Actionable.Styles, { withTheme: true })(withSnackbar(SetLightMode)));