import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractHeading from "../../../gen/displays/components/AbstractHeading";
import HeadingNotifier from "../../../gen/displays/notifiers/HeadingNotifier";
import HeadingRequester from "../../../gen/displays/requesters/HeadingRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class Heading extends AbstractHeading {

	constructor(props) {
		super(props);
		this.notifier = new HeadingNotifier(this);
		this.requester = new HeadingRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(Heading);
DisplayFactory.register("Heading", withStyles(styles, { withTheme: true })(Heading));