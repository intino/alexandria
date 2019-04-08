import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractWidgetSummaryMold from "../../../gen/displays/molds/AbstractWidgetSummaryMold";
import WidgetSummaryMoldNotifier from "../../../gen/displays/notifiers/WidgetSummaryMoldNotifier";
import WidgetSummaryMoldRequester from "../../../gen/displays/requesters/WidgetSummaryMoldRequester";

const styles = theme => ({});

class WidgetSummaryMold extends AbstractWidgetSummaryMold {

	constructor(props) {
		super(props);
		this.notifier = new WidgetSummaryMoldNotifier(this);
		this.requester = new WidgetSummaryMoldRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(WidgetSummaryMold);