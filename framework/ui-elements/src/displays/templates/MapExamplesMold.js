import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractMapExamplesMold from "../../../gen/displays/templates/AbstractMapExamplesMold";
import MapExamplesMoldNotifier from "../../../gen/displays/notifiers/MapExamplesMoldNotifier";
import MapExamplesMoldRequester from "../../../gen/displays/requesters/MapExamplesMoldRequester";

const styles = theme => ({});

class MapExamplesMold extends AbstractMapExamplesMold {

	constructor(props) {
		super(props);
		this.notifier = new MapExamplesMoldNotifier(this);
		this.requester = new MapExamplesMoldRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(MapExamplesMold);