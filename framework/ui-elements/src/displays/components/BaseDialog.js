import React from "react";
import { DialogTitle } from "@material-ui/core"
import AbstractBaseDialog from "../../../gen/displays/components/AbstractBaseDialog";
import BaseDialogNotifier from "../../../gen/displays/notifiers/BaseDialogNotifier";
import BaseDialogRequester from "../../../gen/displays/requesters/BaseDialogRequester";

export default class BaseDialog extends AbstractBaseDialog {
	state = {
		opened: false
	};

	constructor(props) {
		super(props);
		this.notifier = new BaseDialogNotifier(this);
		this.requester = new BaseDialogRequester(this);
	};

	handleClose = () => {
		this.close();
	};

	renderTitle = () => {
		return (<DialogTitle>{this.props.title}</DialogTitle>);
	};

	open = () => {
		this.setState({ opened: true });
	};

	close = () => {
		this.setState({ opened: false });
	};

}