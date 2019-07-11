import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractFrame from "../../../gen/displays/components/AbstractFrame";
import FrameNotifier from "../../../gen/displays/notifiers/FrameNotifier";
import FrameRequester from "../../../gen/displays/requesters/FrameRequester";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

const styles = theme => ({});

export default class Frame extends AbstractFrame {

	state = {
		display : null
	};

	constructor(props) {
		super(props);
		this.notifier = new FrameNotifier(this);
		this.requester = new FrameRequester(this);
	};

	render() {
		return (<div style={this.style()}>{this.renderInstances()}</div>);
	};

}

DisplayFactory.register("Frame", Frame);