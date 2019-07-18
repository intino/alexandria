import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractStamp from "../../../gen/displays/components/AbstractStamp";
import StampNotifier from "../../../gen/displays/notifiers/StampNotifier";
import StampRequester from "../../../gen/displays/requesters/StampRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class Stamp extends AbstractStamp {

	constructor(props) {
		super(props);
		this.notifier = new StampNotifier(this);
		this.requester = new StampRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(Stamp);
DisplayFactory.register("Stamp", withStyles(styles, { withTheme: true })(Stamp));