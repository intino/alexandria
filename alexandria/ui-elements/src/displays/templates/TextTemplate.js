import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractTextTemplate from "../../../gen/displays/templates/AbstractTextTemplate";
import TextTemplateNotifier from "../../../gen/displays/notifiers/TextTemplateNotifier";
import TextTemplateRequester from "../../../gen/displays/requesters/TextTemplateRequester";

const styles = theme => ({});

class TextTemplate extends AbstractTextTemplate {

	constructor(props) {
		super(props);
		this.notifier = new TextTemplateNotifier(this);
		this.requester = new TextTemplateRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(TextTemplate);