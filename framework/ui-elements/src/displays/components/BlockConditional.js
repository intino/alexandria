import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractBlockConditional from "../../../gen/displays/components/AbstractBlockConditional";
import BlockConditionalNotifier from "../../../gen/displays/notifiers/BlockConditionalNotifier";
import BlockConditionalRequester from "../../../gen/displays/requesters/BlockConditionalRequester";
import Block from "./Block";
import BlockBehavior from "./behaviors/BlockBehavior";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

export default class BlockConditional extends AbstractBlockConditional {

	state = {
		visible : false,
		loading : false
	};

	constructor(props) {
		super(props);
		this.notifier = new BlockConditionalNotifier(this);
		this.requester = new BlockConditionalRequester(this);
	};

	render() {
        let animation = this.props.animation;
        if (animation != null)
            return BlockBehavior.renderAnimation(animation, this.state.visible, this.renderBlock());
		return this.renderBlock();
	};

	renderBlock = () => {
		let styles = this.style();
		if (this.props.style != null) this.applyStyles(this.props.style, styles);
		return (
			<div style={styles}>
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

	style() {
		var result = super.style();
		if (result == null) result = {};
		result.display = this.state.visible ? "block" : "none";
		if (this._widthDefined()) result.width = this.props.width;
		// if (this._heightDefined()) result.height = this.props.height;
		return result;
	};

	refreshVisibility = (visible) => {
		this.setState({ visible });
	};
}

DisplayFactory.register("BlockConditional", BlockConditional);