import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractSelectorExamplesMold from "../../../gen/displays/templates/AbstractSelectorExamplesMold";
import SelectorExamplesMoldNotifier from "../../../gen/displays/notifiers/SelectorExamplesMoldNotifier";
import SelectorExamplesMoldRequester from "../../../gen/displays/requesters/SelectorExamplesMoldRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';

const styles = theme => ({});

class SelectorExamplesMold extends AbstractSelectorExamplesMold {

	constructor(props) {
		super(props);
		this.notifier = new SelectorExamplesMoldNotifier(this);
		this.requester = new SelectorExamplesMoldRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(withSnackbar(SelectorExamplesMold));
DisplayFactory.register("SelectorExamplesMold", withStyles(styles, { withTheme: true })(withSnackbar(SelectorExamplesMold)));