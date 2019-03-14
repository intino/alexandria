import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractWidgetBlock from "../../../gen/displays/blocks/AbstractWidgetBlock";
import WidgetBlockNotifier from "../../../gen/displays/notifiers/WidgetBlockNotifier";
import WidgetBlockRequester from "../../../gen/displays/requesters/WidgetBlockRequester";

const styles = theme => ({});

class WidgetBlock extends AbstractWidgetBlock {

	constructor(props) {
		super(props);
		this.notifier = new WidgetBlockNotifier(this);
		this.requester = new WidgetBlockRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(WidgetBlock);