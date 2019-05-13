import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractOperationExamplesMold from "../../../gen/displays/templates/AbstractOperationExamplesMold";
import OperationExamplesMoldNotifier from "../../../gen/displays/notifiers/OperationExamplesMoldNotifier";
import OperationExamplesMoldRequester from "../../../gen/displays/requesters/OperationExamplesMoldRequester";

const styles = theme => ({});

class OperationExamplesMold extends AbstractOperationExamplesMold {

	constructor(props) {
		super(props);
		this.notifier = new OperationExamplesMoldNotifier(this);
		this.requester = new OperationExamplesMoldRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(OperationExamplesMold);