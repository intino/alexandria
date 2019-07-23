import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractLocationExamplesMold from "../../../gen/displays/templates/AbstractLocationExamplesMold";
import LocationExamplesMoldNotifier from "../../../gen/displays/notifiers/LocationExamplesMoldNotifier";
import LocationExamplesMoldRequester from "../../../gen/displays/requesters/LocationExamplesMoldRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';

const styles = theme => ({});

class LocationExamplesMold extends AbstractLocationExamplesMold {

	constructor(props) {
		super(props);
		this.notifier = new LocationExamplesMoldNotifier(this);
		this.requester = new LocationExamplesMoldRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(withSnackbar(LocationExamplesMold));
DisplayFactory.register("LocationExamplesMold", withStyles(styles, { withTheme: true })(withSnackbar(LocationExamplesMold)));