import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractOpenPage from "../../../gen/displays/components/AbstractOpenPage";
import OpenPageNotifier from "../../../gen/displays/notifiers/OpenPageNotifier";
import OpenPageRequester from "../../../gen/displays/requesters/OpenPageRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class OpenPage extends AbstractOpenPage {

	constructor(props) {
		super(props);
		this.notifier = new OpenPageNotifier(this);
		this.requester = new OpenPageRequester(this);
	};

	redirect = (value) => {
	};
}

export default withStyles(styles, { withTheme: true })(OpenPage);
DisplayFactory.register("OpenPage", withStyles(styles, { withTheme: true })(OpenPage));