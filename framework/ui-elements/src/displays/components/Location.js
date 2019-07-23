import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractLocation from "../../../gen/displays/components/AbstractLocation";
import LocationNotifier from "../../../gen/displays/notifiers/LocationNotifier";
import LocationRequester from "../../../gen/displays/requesters/LocationRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';

const styles = theme => ({});

class Location extends AbstractLocation {

	constructor(props) {
		super(props);
		this.notifier = new LocationNotifier(this);
		this.requester = new LocationRequester(this);
	};

	render() {
		return this.renderLayer();
	}

}

export default withStyles(styles, { withTheme: true })(withSnackbar(Location));
DisplayFactory.register("Location", withStyles(styles, { withTheme: true })(withSnackbar(Location)));