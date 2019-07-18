import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractBaseDate from "../../../gen/displays/components/AbstractBaseDate";
import BaseDateNotifier from "../../../gen/displays/notifiers/BaseDateNotifier";
import BaseDateRequester from "../../../gen/displays/requesters/BaseDateRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class BaseDate extends AbstractBaseDate {

	constructor(props) {
		super(props);
		this.notifier = new BaseDateNotifier(this);
		this.requester = new BaseDateRequester(this);
	};

	refresh = (value) => {
	};
}

export default withStyles(styles, { withTheme: true })(BaseDate);
DisplayFactory.register("BaseDate", withStyles(styles, { withTheme: true })(BaseDate));