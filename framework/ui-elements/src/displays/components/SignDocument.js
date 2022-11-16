import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractSignDocument from "../../../gen/displays/components/AbstractSignDocument";
import SignDocumentNotifier from "../../../gen/displays/notifiers/SignDocumentNotifier";
import SignDocumentRequester from "../../../gen/displays/requesters/SignDocumentRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import Actionable from "./Actionable";
import { withSnackbar } from 'notistack';

const styles = theme => ({});

class SignDocument extends AbstractSignDocument {

	constructor(props) {
		super(props);
		this.notifier = new SignDocumentNotifier(this);
		this.requester = new SignDocumentRequester(this);
	};

	document = (documentData) => {
	    this.setState({ data: documentData });
	};

    sign = (content) => {
        this.requester.signing();
        this.behavior.signDocument(content, this._successCallback.bind(this), this._failureCallback.bind(this));
    };
}

export default withStyles(Actionable.Styles, { withTheme: true })(withSnackbar(SignDocument));
DisplayFactory.register("SignDocument", withStyles(Actionable.Styles, { withTheme: true })(withSnackbar(SignDocument)));