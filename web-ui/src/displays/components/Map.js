import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractMap from "../../../gen/displays/components/AbstractMap";
import MapNotifier from "../../../gen/displays/notifiers/MapNotifier";
import MapRequester from "../../../gen/displays/requesters/MapRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class Map extends AbstractMap {

	constructor(props) {
		super(props);
		this.notifier = new MapNotifier(this);
		this.requester = new MapRequester(this);
	};

	setup = (value) => {
	};

	placeMarks = (value) => {
	};
}

export default withStyles(styles, { withTheme: true })(Map);
DisplayFactory.register("Map", withStyles(styles, { withTheme: true })(Map));