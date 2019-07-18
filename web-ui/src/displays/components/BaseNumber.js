import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractBaseNumber from "../../../gen/displays/components/AbstractBaseNumber";
import BaseNumberNotifier from "../../../gen/displays/notifiers/BaseNumberNotifier";
import BaseNumberRequester from "../../../gen/displays/requesters/BaseNumberRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class BaseNumber extends AbstractBaseNumber {

	constructor(props) {
		super(props);
		this.notifier = new BaseNumberNotifier(this);
		this.requester = new BaseNumberRequester(this);
	};

	refresh = (value) => {
	};
}

export default withStyles(styles, { withTheme: true })(BaseNumber);
DisplayFactory.register("BaseNumber", withStyles(styles, { withTheme: true })(BaseNumber));