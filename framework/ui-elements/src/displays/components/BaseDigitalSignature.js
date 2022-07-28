import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractBaseDigitalSignature from "../../../gen/displays/components/AbstractBaseDigitalSignature";
import BaseDigitalSignatureNotifier from "../../../gen/displays/notifiers/BaseDigitalSignatureNotifier";
import BaseDigitalSignatureRequester from "../../../gen/displays/requesters/BaseDigitalSignatureRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';

const styles = theme => ({});

export default class BaseDigitalSignature extends AbstractBaseDigitalSignature {

	constructor(props) {
		super(props);
		this.notifier = new BaseDigitalSignatureNotifier(this);
		this.requester = new BaseDigitalSignatureRequester(this);
		this.state = {
		    ...this.state,
		    data: null,
		    isDocument: false,
		    signing: false,
		    readonly: false,
		};
	};

    text = (content) => {
        this.setState({ data: content, isDocument: false });
    };

    sign = () => {
    };

    document = (content) => {
        this.setState({ data: content, isDocument: true });
    };

    refreshReadonly = (value) => {
        this.setState({readonly: value});
    };

}