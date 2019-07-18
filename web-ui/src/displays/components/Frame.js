import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractFrame from "../../../gen/displays/components/AbstractFrame";
import FrameNotifier from "../../../gen/displays/notifiers/FrameNotifier";
import FrameRequester from "../../../gen/displays/requesters/FrameRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class Frame extends AbstractFrame {

	constructor(props) {
		super(props);
		this.notifier = new FrameNotifier(this);
		this.requester = new FrameRequester(this);
	};

	refreshDisplay = (value) => {
	};
}

export default withStyles(styles, { withTheme: true })(Frame);
DisplayFactory.register("Frame", withStyles(styles, { withTheme: true })(Frame));