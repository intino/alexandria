import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractNumber from "../../../gen/displays/components/AbstractNumber";
import NumberNotifier from "../../../gen/displays/notifiers/NumberNotifier";
import NumberRequester from "../../../gen/displays/requesters/NumberRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class Number extends AbstractNumber {

	constructor(props) {
		super(props);
		this.notifier = new NumberNotifier(this);
		this.requester = new NumberRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(Number);
DisplayFactory.register("Number", withStyles(styles, { withTheme: true })(Number));