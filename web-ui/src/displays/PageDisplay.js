import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractPageDisplay from "../../gen/displays/AbstractPageDisplay";
import PageDisplayNotifier from "../../gen/displays/notifiers/PageDisplayNotifier";
import PageDisplayRequester from "../../gen/displays/requesters/PageDisplayRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class PageDisplay extends AbstractPageDisplay {

	constructor(props) {
		super(props);
		this.notifier = new PageDisplayNotifier(this);
		this.requester = new PageDisplayRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(PageDisplay);
DisplayFactory.register("PageDisplay", withStyles(styles, { withTheme: true })(PageDisplay));