import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractSignText from "../../../gen/displays/components/AbstractSignText";
import SignTextNotifier from "../../../gen/displays/notifiers/SignTextNotifier";
import SignTextRequester from "../../../gen/displays/requesters/SignTextRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import Actionable from "./Actionable";
import { withSnackbar } from 'notistack';

const styles = theme => ({});

class SignText extends AbstractSignText {

	constructor(props) {
		super(props);
		this.notifier = new SignTextNotifier(this);
		this.requester = new SignTextRequester(this);
	};

    sign = (content) => {
        this.requester.signing();
        this.behavior.sign(content, this.state.format, this._successCallback.bind(this), this._failureCallback.bind(this));
    };

}

export default withStyles(Actionable.Styles, { withTheme: true })(withSnackbar(SignText));
DisplayFactory.register("SignText", withStyles(Actionable.Styles, { withTheme: true })(withSnackbar(SignText)));