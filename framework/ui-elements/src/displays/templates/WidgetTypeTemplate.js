import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractWidgetTypeTemplate from "../../../gen/displays/templates/AbstractWidgetTypeTemplate";
import WidgetTypeTemplateNotifier from "../../../gen/displays/notifiers/WidgetTypeTemplateNotifier";
import WidgetTypeTemplateRequester from "../../../gen/displays/requesters/WidgetTypeTemplateRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class WidgetTypeTemplate extends AbstractWidgetTypeTemplate {

	constructor(props) {
		super(props);
		this.notifier = new WidgetTypeTemplateNotifier(this);
		this.requester = new WidgetTypeTemplateRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(WidgetTypeTemplate);
DisplayFactory.register("WidgetTypeTemplate", withStyles(styles, { withTheme: true })(WidgetTypeTemplate));