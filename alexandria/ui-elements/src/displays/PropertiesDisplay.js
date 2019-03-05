import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractPropertiesDisplay from "../../gen/displays/AbstractPropertiesDisplay";
import PropertiesDisplayNotifier from "../../gen/displays/notifiers/PropertiesDisplayNotifier";
import PropertiesDisplayRequester from "../../gen/displays/requesters/PropertiesDisplayRequester";

const styles = theme => ({});

class PropertiesDisplay extends AbstractPropertiesDisplay {

	constructor(props) {
		super(props);
		this.notifier = new PropertiesDisplayNotifier(this);
		this.requester = new PropertiesDisplayRequester(this);
	};

	refresh = (value) => {
	};
}

export default withStyles(styles, { withTheme: true })(PropertiesDisplay);