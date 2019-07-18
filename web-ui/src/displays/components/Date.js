import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractDate from "../../../gen/displays/components/AbstractDate";
import DateNotifier from "../../../gen/displays/notifiers/DateNotifier";
import DateRequester from "../../../gen/displays/requesters/DateRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class Date extends AbstractDate {

	constructor(props) {
		super(props);
		this.notifier = new DateNotifier(this);
		this.requester = new DateRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(Date);
DisplayFactory.register("Date", withStyles(styles, { withTheme: true })(Date));