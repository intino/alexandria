import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractFrame from "../../../gen/displays/components/AbstractFrame";
import FrameNotifier from "../../../gen/displays/notifiers/FrameNotifier";
import FrameRequester from "../../../gen/displays/requesters/FrameRequester";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

const styles = theme => ({});

export default class Frame extends AbstractFrame {

	constructor(props) {
		super(props);
		this.notifier = new FrameNotifier(this);
		this.requester = new FrameRequester(this);
        this.state = {
            ...this.state,
            display : null
        };
	};

	render() {
		if (!this.state.visible) return (<React.Fragment/>);
		return (<div style={this.style()}>{this.renderInstances(null, null, this.style())}</div>);
	};

}

DisplayFactory.register("Frame", Frame);