import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractRow from "../../../gen/displays/components/AbstractRow";
import RowNotifier from "../../../gen/displays/notifiers/RowNotifier";
import RowRequester from "../../../gen/displays/requesters/RowRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class Row extends AbstractRow {

	constructor(props) {
		super(props);
		this.notifier = new RowNotifier(this);
		this.requester = new RowRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(Row);
DisplayFactory.register("Row", withStyles(styles, { withTheme: true })(Row));