import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractSelectionOperation from "../../../gen/displays/components/AbstractSelectionOperation";
import SelectionOperationNotifier from "../../../gen/displays/notifiers/SelectionOperationNotifier";
import SelectionOperationRequester from "../../../gen/displays/requesters/SelectionOperationRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class SelectionOperation extends AbstractSelectionOperation {

	constructor(props) {
		super(props);
		this.notifier = new SelectionOperationNotifier(this);
		this.requester = new SelectionOperationRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(SelectionOperation);
DisplayFactory.register("SelectionOperation", withStyles(styles, { withTheme: true })(SelectionOperation));