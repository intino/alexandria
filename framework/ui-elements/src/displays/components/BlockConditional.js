import React from "react";
import AbstractBlockConditional from "../../../gen/displays/components/AbstractBlockConditional";
import BlockConditionalNotifier from "../../../gen/displays/notifiers/BlockConditionalNotifier";
import BlockConditionalRequester from "../../../gen/displays/requesters/BlockConditionalRequester";
import Block from "./Block";
import BlockBehavior from "./behaviors/BlockBehavior";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";
import BrowserUtil from "../../util/BrowserUtil";
import 'alexandria-ui-elements/res/styles/layout.css';
import 'alexandria-ui-elements/res/styles/mobile.css';

export default class BlockConditional extends AbstractBlockConditional {

	constructor(props) {
		super(props);
		this.notifier = new BlockConditionalNotifier(this);
		this.requester = new BlockConditionalRequester(this);
		this.state = {
			...this.state,
			visible: false,
			loading: false,
		}
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
		const classNames = BlockBehavior.classNames(this);
		return (
			<div style={styles} className={classNames}>
				<Block label={this.props.label}
					   /*style={this.style()}*/
					   layout={this.props.layout}
					   /*width={this.props.width}
					   height={this.props.height}*/
					   spacing={this.props.spacing}>
					{this.state.visible && this.props.children}
				</Block>
			</div>
		);
	};

	style() {
		var result = super.style();
		if (result == null) result = {};
		result.display = this.state.visible ? "" : "none";
		if (this._widthDefined()) result.width = this.props.width;
		if (this._heightDefined()) result.height = this.props.height;
		if (result.height === "100.0%" && BrowserUtil.isFirefox()) result.height = "100vh";
		return result;
	};

}

DisplayFactory.register("BlockConditional", BlockConditional);