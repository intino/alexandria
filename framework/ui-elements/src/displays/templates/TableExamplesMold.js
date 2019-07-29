import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractTableExamplesMold from "../../../gen/displays/templates/AbstractTableExamplesMold";
import TableExamplesMoldNotifier from "../../../gen/displays/notifiers/TableExamplesMoldNotifier";
import TableExamplesMoldRequester from "../../../gen/displays/requesters/TableExamplesMoldRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class TableExamplesMold extends AbstractTableExamplesMold {

	constructor(props) {
		super(props);
		this.notifier = new TableExamplesMoldNotifier(this);
		this.requester = new TableExamplesMoldRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(TableExamplesMold);
DisplayFactory.register("TableExamplesMold", withStyles(styles, { withTheme: true })(TableExamplesMold));