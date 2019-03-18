import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractPropertiesBlock from "../../../gen/displays/blocks/AbstractPropertiesBlock";
import PropertiesBlockNotifier from "../../../gen/displays/notifiers/PropertiesBlockNotifier";
import PropertiesBlockRequester from "../../../gen/displays/requesters/PropertiesBlockRequester";

const styles = theme => ({});

class PropertiesBlock extends AbstractPropertiesBlock {

	constructor(props) {
		super(props);
		this.notifier = new PropertiesBlockNotifier(this);
		this.requester = new PropertiesBlockRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(PropertiesBlock);