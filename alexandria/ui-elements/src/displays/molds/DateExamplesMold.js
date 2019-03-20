import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractDateExamplesMold from "../../../gen/displays/molds/AbstractDateExamplesMold";
import DateExamplesMoldNotifier from "../../../gen/displays/notifiers/DateExamplesMoldNotifier";
import DateExamplesMoldRequester from "../../../gen/displays/requesters/DateExamplesMoldRequester";

const styles = theme => ({});

class DateExamplesMold extends AbstractDateExamplesMold {

	constructor(props) {
		super(props);
		this.notifier = new DateExamplesMoldNotifier(this);
		this.requester = new DateExamplesMoldRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(DateExamplesMold);