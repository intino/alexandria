import React from "react";
import Collapse from "@material-ui/core/Collapse";
import Paper from "@material-ui/core/Paper";
import AbstractBlock from "../../../gen/displays/components/AbstractBlock";
import BlockNotifier from "../../../gen/displays/notifiers/BlockNotifier";
import BlockRequester from "../../../gen/displays/requesters/BlockRequester";
import classNames from "classnames";
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
		let paper = this.props.paper;
		let layout = this._layout();
		let style = this.style();

		if (paper)
			return (<Paper style={style} className={layout}>{this._renderChildren()}</Paper>);

		return (<div style={style} className={layout}>{this._renderChildren()}</div>);
	};

	_renderChildren = () => {
		const vertical = this._is("vertical");
		const hasSpacing = this._hasSpacing();

		return React.Children.map(this.props.children, (child, i) => {
			if (hasSpacing) {
				let spacingStyle = (vertical ? "bottom:" : "right:") + this.props.spacing;
				return React.cloneElement(child, { spacingStyle: spacingStyle });
			}
			return child;
		});
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
		if (this._is("vertical") && this._is("center")) result.margin = "0 auto";
		if (this.props.style != null) this.applyStyles(this.props.style, result);
		if (this.props.margin != null) result.margin = this.props.margin;
		if (this._widthDefined()) result.width = this.props.width;
		if (this._heightDefined()) result.height = this.props.height;
		return result;
	};

	_is = (layout) => {
		if (this.props.layout == null) return false;
		return (" " + this.props.layout + " ").indexOf(" " + layout + " ") !== -1;
	};

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