import React from "react";
import Collapse from "@material-ui/core/Collapse";
import AbstractBlock from "../../../gen/displays/components/AbstractBlock";
import BlockNotifier from "../../../gen/displays/notifiers/BlockNotifier";
import BlockRequester from "../../../gen/displays/requesters/BlockRequester";
import 'alexandria-ui-elements/res/styles/layout.css';

export default class Block extends AbstractBlock {
	state = {
		hidden: false
	};

	constructor(props) {
		super(props);
		this.notifier = new BlockNotifier(this);
		this.requester = new BlockRequester(this);
	};

	render() {
		return (this.props.collapsible ? <Collapse>{this._renderLayout()}</Collapse> : this._renderLayout());
	};

	_renderLayout = () => {
		const hasSpacing = this._hasSpacing();

		return (
			<div style={this.style()} className={this._layout()}>
				{React.Children.map(this.props.children, (child, i) => {
					if (hasSpacing) {
						var style = this._is("vertical") ? { marginBottom: this.props.spacing + "px" } : { marginRight: this.props.spacing + "px" };
						return React.cloneElement(child, { style: style });
					}
					return child;
				})}
			</div>
		);
	};

	_layout = () => {
		let layout = this.props.layout;
		layout = layout.replace("flexible", "flex");
		layout = layout.replace("centercenter", "center-center");
		layout = layout.replace("preverse", "p-reverse");
		layout = layout.replace("lreverse", "l-reverse");
		layout = layout.replace("tjustified", "t-justified");
		layout = layout.replace("djustified", "d-justified");
		layout = layout.replace("nowrap", "no-wrap");
		if (layout === "vertical") return undefined;
		return "layout " + layout;
	};

	style() {
		var result = super.style();
		if (result == null) result = {};
		if (this.props.style != null) this.applyStyles(this.props.style, result);
		if (this.props.margin != null) result.margin = this.props.margin;
		if (this._widthDefined()) result.width = this.props.width;
		if (this._heightDefined()) result.height = this.props.height;
		return result;
	};

	_is = (layout) => {
		if (this.props.layout == null) return false;
		return (" " + this.props.layout + " ").indexOf(" " + layout + " ") !== -1;
	}

	_hasSpacing = () => {
		return this.props.spacing != null;
	};

	_widthDefined = () => {
		return this.props.width != null && this.props.width.indexOf("-1") === -1;
	};

	_heightDefined = () => {
		return this.props.height != null && this.props.height.indexOf("-1") === -1;
	}
}