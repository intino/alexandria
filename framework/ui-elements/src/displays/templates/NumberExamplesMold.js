import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractNumberExamplesMold from "../../../gen/displays/templates/AbstractNumberExamplesMold";
import NumberExamplesMoldNotifier from "../../../gen/displays/notifiers/NumberExamplesMoldNotifier";
import NumberExamplesMoldRequester from "../../../gen/displays/requesters/NumberExamplesMoldRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class NumberExamplesMold extends AbstractNumberExamplesMold {

	constructor(props) {
		super(props);
		this.notifier = new NumberExamplesMoldNotifier(this);
		this.requester = new NumberExamplesMoldRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(NumberExamplesMold);
DisplayFactory.register("NumberExamplesMold", withStyles(styles, { withTheme: true })(NumberExamplesMold));