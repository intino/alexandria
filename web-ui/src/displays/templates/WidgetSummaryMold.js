import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractWidgetSummaryMold from "../../../gen/displays/templates/AbstractWidgetSummaryMold";
import WidgetSummaryMoldNotifier from "../../../gen/displays/notifiers/WidgetSummaryMoldNotifier";
import WidgetSummaryMoldRequester from "../../../gen/displays/requesters/WidgetSummaryMoldRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class WidgetSummaryMold extends AbstractWidgetSummaryMold {

	constructor(props) {
		super(props);
		this.notifier = new WidgetSummaryMoldNotifier(this);
		this.requester = new WidgetSummaryMoldRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(WidgetSummaryMold);
DisplayFactory.register("WidgetSummaryMold", withStyles(styles, { withTheme: true })(WidgetSummaryMold));