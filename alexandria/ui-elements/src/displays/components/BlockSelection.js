import React from "react";
import AbstractBlockSelection from "../../../gen/displays/components/AbstractBlockSelection";
import BlockSelectionNotifier from "../../../gen/displays/notifiers/BlockSelectionNotifier";
import BlockSelectionRequester from "../../../gen/displays/requesters/BlockSelectionRequester";
import Block from "./Block";

export default class BlockSelection extends AbstractBlockSelection {
	state = {
		visible : false
	};

	constructor(props) {
		super(props);
		this.notifier = new BlockSelectionNotifier(this);
		this.requester = new BlockSelectionRequester(this);
	};

	render() {
		return (
			<div style={ { display: this.state.visible ? "block" : "none" } }>
				<Block styleName={this.props.styleName}
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