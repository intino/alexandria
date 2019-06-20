import React from "react";
import Collapse from '@material-ui/core/Collapse';
import Paper from "@material-ui/core/Paper";
import Typography from "@material-ui/core/Typography";
import AbstractBlock from "../../../gen/displays/components/AbstractBlock";
import BlockNotifier from "../../../gen/displays/notifiers/BlockNotifier";
import BlockRequester from "../../../gen/displays/requesters/BlockRequester";
import BlockBehavior from "./behaviors/BlockBehavior";
import 'alexandria-ui-elements/res/styles/layout.css';
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

export default class Block extends AbstractBlock {
	state = {
		hidden: false,
		layout: this.props.layout,
		spacing: this.props.spacing
	};

	constructor(props) {
		super(props);
		this.notifier = new BlockNotifier(this);
		this.requester = new BlockRequester(this);
	};

	render() {
		let animation = this.props.animation;
		if (animation != null)
			return BlockBehavior.renderAnimation(animation, !this.state.hidden, this.renderContent());
		return this.renderContent();
	};

	renderContent = () => {
		return (this.props.collapsible ? <Collapse>{this._renderLayout()}</Collapse> : this._renderLayout());
	};

	refreshSpacing = (spacing) => {
		this.setState({ spacing });
	};

	refreshLayout = (layout) => {
		this.setState({ layout });
	};

	_renderLayout = () => {
		let paper = this.props.paper;
		let style = this.style();
		let label = this.props.label;
		const layout = BlockBehavior.className(this);

		if (paper) {
			return (
				<Paper style={style} className={layout}>
					{label != null && label !== "" ? <Typography style={{padding:"0 10px"}} variant={this.variant("h5")}>{label}</Typography> : undefined }
					<div style={{padding:"0 10px 10px"}}>{this._renderChildren()}</div>
				</Paper>
			);
		}

		return (
			<div style={style} className={layout}>
				{label != null && label !== "" ? <Typography style={{padding:"0 0 5px"}} variant={this.variant("h5")}>{label}</Typography> : undefined }
				{this._renderChildren()}
			</div>
		);
	};

	_renderChildren = () => {
		const hasSpacing = this._hasSpacing();

		return React.Children.map(this.props.children, (child, i) => {
			if (hasSpacing) {
				let spacing = this._spacing();
				let props = child.type !== "div" ? { spacingstyle: spacing } : { style: { marginBottom: spacing.bottom + "px", marginRight: spacing.right + "px" } };
				return React.cloneElement(child, props);
			}
			return child;
		});
	};

	style() {
		var result = super.style();
		if (result == null) result = {};
		if (this._is("vertical") && this._is("center")) result.margin = "0 auto";
		if (this.props.style != null) this.applyStyles(this.props.style, result);
		if (this.props.margin != null) result.margin = this.props.margin;
		if (this._widthDefined()) result.width = this.props.width;
		if (this._heightDefined()) result.height = this.props.height;
		return result;
	};

	_is = (layout) => {
		if (this.state.layout == null) return false;
		return (" " + this.state.layout + " ").indexOf(" " + layout + " ") !== -1;
	};

	_spacing() {
		if (!this._hasSpacing()) return null;
		const withBottom = this._is("vertical") || (this._is("horizontal") && this._is("wrap"));
		let spacingSize = this.state.spacing != null ? this.state.spacing : this.props.spacing;
		let spacingStyle = { right: spacingSize };
		if (withBottom) spacingStyle.bottom = spacingSize;
		return spacingStyle;
	};

	_hasSpacing = () => {
		return this.state.spacing != null || this.props.spacing != null;
	};

}

DisplayFactory.register("Block", Block);
