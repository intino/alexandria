import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractPropertyBlock from "../../../gen/displays/blocks/AbstractPropertyBlock";
import PropertyBlockNotifier from "../../../gen/displays/notifiers/PropertyBlockNotifier";
import PropertyBlockRequester from "../../../gen/displays/requesters/PropertyBlockRequester";

const styles = theme => ({});

class PropertyBlock extends AbstractPropertyBlock {

	constructor(props) {
		super(props);
		this.notifier = new PropertyBlockNotifier(this);
		this.requester = new PropertyBlockRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(PropertyBlock);