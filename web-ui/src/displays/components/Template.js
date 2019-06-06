import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractTemplate from "../../../gen/displays/components/AbstractTemplate";
import TemplateNotifier from "../../../gen/displays/notifiers/TemplateNotifier";
import TemplateRequester from "../../../gen/displays/requesters/TemplateRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class Template extends AbstractTemplate {

	constructor(props) {
		super(props);
		this.notifier = new TemplateNotifier(this);
		this.requester = new TemplateRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(Template);
DisplayFactory.register("Template", withStyles(styles, { withTheme: true })(Template));