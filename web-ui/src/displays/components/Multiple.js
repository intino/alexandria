import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractMultiple from "../../../gen/displays/components/AbstractMultiple";
import MultipleNotifier from "../../../gen/displays/notifiers/MultipleNotifier";
import MultipleRequester from "../../../gen/displays/requesters/MultipleRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class Multiple extends AbstractMultiple {

	constructor(props) {
		super(props);
		this.notifier = new MultipleNotifier(this);
		this.requester = new MultipleRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(Multiple);
DisplayFactory.register("Multiple", withStyles(styles, { withTheme: true })(Multiple));