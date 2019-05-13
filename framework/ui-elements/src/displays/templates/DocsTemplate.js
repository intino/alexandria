import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractDocsTemplate from "../../../gen/displays/templates/AbstractDocsTemplate";
import DocsTemplateNotifier from "../../../gen/displays/notifiers/DocsTemplateNotifier";
import DocsTemplateRequester from "../../../gen/displays/requesters/DocsTemplateRequester";

const styles = theme => ({});

class DocsTemplate extends AbstractDocsTemplate {

	constructor(props) {
		super(props);
		this.notifier = new DocsTemplateNotifier(this);
		this.requester = new DocsTemplateRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(DocsTemplate);