import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractSpinner from "../../../gen/displays/components/AbstractSpinner";
import SpinnerNotifier from "../../../gen/displays/notifiers/SpinnerNotifier";
import SpinnerRequester from "../../../gen/displays/requesters/SpinnerRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class Spinner extends AbstractSpinner {

	constructor(props) {
		super(props);
		this.notifier = new SpinnerNotifier(this);
		this.requester = new SpinnerRequester(this);
	};

	refreshLoading = (value) => {
	};
}

export default withStyles(styles, { withTheme: true })(Spinner);
DisplayFactory.register("Spinner", withStyles(styles, { withTheme: true })(Spinner));