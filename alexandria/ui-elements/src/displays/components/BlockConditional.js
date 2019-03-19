import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractBlockConditional from "../../../gen/displays/components/AbstractBlockConditional";
import BlockConditionalNotifier from "../../../gen/displays/notifiers/BlockConditionalNotifier";
import BlockConditionalRequester from "../../../gen/displays/requesters/BlockConditionalRequester";
import Block from "./Block";

export default class BlockConditional extends AbstractBlockConditional {

	state = {
		visible : false
	};

	constructor(props) {
		super(props);
		this.notifier = new BlockConditionalNotifier(this);
		this.requester = new BlockConditionalRequester(this);
	};

	render() {
		return (
			<div style={ { display: this.state.visible ? "block" : "none" } }>
				<Block style={this.style()}
					   layout={this.props.layout}
					   width={this.props.width}
					   height={this.props.height}
					   spacing={this.props.spacing}>
					{this.props.children}
				</Block>
			</div>
		);
	};

	refreshVisibility = (visible) => {
		this.setState({ visible });
	};
}