import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractCopyToClipboard from "../../../gen/displays/components/AbstractCopyToClipboard";
import CopyToClipboardNotifier from "../../../gen/displays/notifiers/CopyToClipboardNotifier";
import CopyToClipboardRequester from "../../../gen/displays/requesters/CopyToClipboardRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';
import Actionable from "./Actionable";

class CopyToClipboard extends AbstractCopyToClipboard {

	constructor(props) {
		super(props);
		this.notifier = new CopyToClipboardNotifier(this);
		this.requester = new CopyToClipboardRequester(this);
	};

	copy = (content) => {
        navigator.clipboard.writeText(content);
        this.requester.copied(true);
	};
}

export default withStyles(Actionable.Styles, { withTheme: true })(withSnackbar(CopyToClipboard));
DisplayFactory.register("CopyToClipboard", withStyles(Actionable.Styles, { withTheme: true })(withSnackbar(CopyToClipboard)));