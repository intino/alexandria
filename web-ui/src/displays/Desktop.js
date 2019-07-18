import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractDesktop from "../../gen/displays/AbstractDesktop";
import DesktopNotifier from "../../gen/displays/notifiers/DesktopNotifier";
import DesktopRequester from "../../gen/displays/requesters/DesktopRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class Desktop extends AbstractDesktop {

	constructor(props) {
		super(props);
		this.notifier = new DesktopNotifier(this);
		this.requester = new DesktopRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(Desktop);
DisplayFactory.register("Desktop", withStyles(styles, { withTheme: true })(Desktop));