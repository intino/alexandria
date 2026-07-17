import React from "react";
import {withStyles} from 'alexandria-ui-elements/src/util/muiStylesCompat';
import AbstractLocation from "../../../gen/displays/components/AbstractLocation";
import LocationNotifier from "../../../gen/displays/notifiers/LocationNotifier";
import LocationRequester from "../../../gen/displays/requesters/LocationRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import {withSnackbar} from "alexandria-ui-elements/src/util/notistackCompat";

const styles = theme => ({});

class Location extends AbstractLocation {

	constructor(props) {
		super(props);
		this.notifier = new LocationNotifier(this);
		this.requester = new LocationRequester(this);
	};

	render() {
	    if (!this.state.visible) return (<React.Fragment/>);
		return this.renderLayer();
	}

}

export default withStyles(styles, { withTheme: true })(withSnackbar(Location));
DisplayFactory.register("Location", withStyles(styles, { withTheme: true })(withSnackbar(Location)));