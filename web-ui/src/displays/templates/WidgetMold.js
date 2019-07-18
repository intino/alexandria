import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractWidgetMold from "../../../gen/displays/templates/AbstractWidgetMold";
import WidgetMoldNotifier from "../../../gen/displays/notifiers/WidgetMoldNotifier";
import WidgetMoldRequester from "../../../gen/displays/requesters/WidgetMoldRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class WidgetMold extends AbstractWidgetMold {

	constructor(props) {
		super(props);
		this.notifier = new WidgetMoldNotifier(this);
		this.requester = new WidgetMoldRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(WidgetMold);
DisplayFactory.register("WidgetMold", withStyles(styles, { withTheme: true })(WidgetMold));