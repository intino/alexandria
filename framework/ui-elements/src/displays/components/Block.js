import React from "react";
import Paper from "@material-ui/core/Paper";
import Typography from "@material-ui/core/Typography";
import AbstractBlock from "../../../gen/displays/components/AbstractBlock";
import BlockNotifier from "../../../gen/displays/notifiers/BlockNotifier";
import BlockRequester from "../../../gen/displays/requesters/BlockRequester";
import BlockBehavior from "./behaviors/BlockBehavior";
import ComponentBehavior from "./behaviors/ComponentBehavior";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";
import BrowserUtil from "../../util/BrowserUtil";
import 'alexandria-ui-elements/res/styles/layout.css';
import 'alexandria-ui-elements/res/styles/hidden.css';
import AutoSizer from 'react-virtualized-auto-sizer';

export default class Block extends AbstractBlock {

	constructor(props) {
		super(props);
		this.notifier = new BlockNotifier(this);
		this.requester = new BlockRequester(this);
		this.state = {
			hidden: false,
			layout: this.props.layout,
			spacing: this.props.spacing,
			autoSize: this.props.autoSize != null ? this.props.autoSize : false,
			...this.state
		}
	};

	render() {
	    if (!this.state.visible) return (<React.Fragment/>);
	    const content = this.renderContent();
	    if (!this.state.autoSize) return content;
	    return (
	        <div style={{position:'relative',height:'100%'}}>
	            <AutoSizer>
	                {({ height, width }) => (
	                    <div style={{height:height+"px",width:(width-1)+"px",overflow:'auto'}}>{content}</div>
                    )}
                </AutoSizer>
	        </div>
        );
	};

    renderContent = () => {
		let animation = this.props.animation;
		if (animation != null)
			return BlockBehavior.renderAnimation(animation, !this.state.hidden, this.renderBody());
		return this.renderBody();
    };

	renderBody = () => {
	    return this._renderLayout();
	};

	refreshSpacing = (spacing) => {
		this.setState({ spacing });
	};

	refreshLayout = (layout) => {
		this.setState({ layout });
	};

	refreshAutoSize = (autoSize) => {
		this.setState({ autoSize });
	};

	_renderLayout = () => {
		let paper = this.props.paper;
		let style = this.style();
		const classNames = BlockBehavior.classNames(this);

		if (!this.state.visible) style = {display:"none", ...style};

		if (paper) {
			return (
				<Paper style={style} className={classNames}>
					{ ComponentBehavior.labelBlock(this.props, "h5", {padding:"0 0 5px"}) }
					<div style={{padding:"0 10px 10px",height:"100%"}} className={classNames}>{this._renderChildren()}</div>
				</Paper>
			);
		}

		return (
			<div style={style} className={classNames}>
				{ ComponentBehavior.labelBlock(this.props, "h5", {padding:"0 0 5px"}) }
				{this._renderChildren()}
			</div>
		);
	};

	_renderChildren = () => {
		const hasSpacing = this._hasSpacing();

		return React.Children.map(this.props.children, (child, i) => {
            if (child == null) return (<React.Fragment/>);
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
		if (this._widthDefined() && result.width == null) result.width = this.props.width;
		if (this._heightDefined() && result.height == null) result.height = this.props.height;
		if (result.height === "100.0%" && BrowserUtil.isFirefox() && this._is("vertical")) result.height = "100vh";
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