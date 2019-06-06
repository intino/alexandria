import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractPage from "../../gen/displays/AbstractPage";
import PageNotifier from "../../gen/displays/notifiers/PageNotifier";
import PageRequester from "../../gen/displays/requesters/PageRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class Page extends AbstractPage {

	constructor(props) {
		super(props);
		this.notifier = new PageNotifier(this);
		this.requester = new PageRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(Page);
DisplayFactory.register("Page", withStyles(styles, { withTheme: true })(Page));