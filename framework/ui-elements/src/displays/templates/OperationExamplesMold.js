import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractOperationExamplesMold from "../../../gen/displays/templates/AbstractOperationExamplesMold";
import OperationExamplesMoldNotifier from "../../../gen/displays/notifiers/OperationExamplesMoldNotifier";
import OperationExamplesMoldRequester from "../../../gen/displays/requesters/OperationExamplesMoldRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class OperationExamplesMold extends AbstractOperationExamplesMold {

	constructor(props) {
		super(props);
		this.notifier = new OperationExamplesMoldNotifier(this);
		this.requester = new OperationExamplesMoldRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(OperationExamplesMold);
DisplayFactory.register("OperationExamplesMold", withStyles(styles, { withTheme: true })(OperationExamplesMold));