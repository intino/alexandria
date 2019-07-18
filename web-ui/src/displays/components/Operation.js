import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractOperation from "../../../gen/displays/components/AbstractOperation";
import OperationNotifier from "../../../gen/displays/notifiers/OperationNotifier";
import OperationRequester from "../../../gen/displays/requesters/OperationRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class Operation extends AbstractOperation {

	constructor(props) {
		super(props);
		this.notifier = new OperationNotifier(this);
		this.requester = new OperationRequester(this);
	};

	refresh = (value) => {
	};

	refreshDisabled = (value) => {
	};

	refreshIcon = (value) => {
	};
}

export default withStyles(styles, { withTheme: true })(Operation);
DisplayFactory.register("Operation", withStyles(styles, { withTheme: true })(Operation));