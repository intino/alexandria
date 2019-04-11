import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractWidgetMold from "../../../gen/displays/molds/AbstractWidgetMold";
import WidgetMoldNotifier from "../../../gen/displays/notifiers/WidgetMoldNotifier";
import WidgetMoldRequester from "../../../gen/displays/requesters/WidgetMoldRequester";

const styles = theme => ({});

class WidgetMold extends AbstractWidgetMold {

	constructor(props) {
		super(props);
		this.notifier = new WidgetMoldNotifier(this);
		this.requester = new WidgetMoldRequester(this);
	};

}

export default withStyles(styles, { withTheme: true })(WidgetMold);