import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractTable from "../../../gen/displays/components/AbstractTable";
import TableNotifier from "../../../gen/displays/notifiers/TableNotifier";
import TableRequester from "../../../gen/displays/requesters/TableRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class Table extends AbstractTable {

	constructor(props) {
		super(props);
		this.notifier = new TableNotifier(this);
		this.requester = new TableRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(Table);
DisplayFactory.register("Table", withStyles(styles, { withTheme: true })(Table));