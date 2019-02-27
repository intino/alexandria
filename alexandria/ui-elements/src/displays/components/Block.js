import React from "react";
import Grid from "@material-ui/core/Grid";
import AbstractBlock from "../../../gen/displays/components/AbstractBlock";
import BlockNotifier from "../../../gen/displays/notifiers/BlockNotifier";
import BlockRequester from "../../../gen/displays/requesters/BlockRequester";
import {withStyles} from "@material-ui/core";

const styles = theme => ({});

class Block extends AbstractBlock {
	state = {
		hidden: false
	};

	constructor(props) {
		super(props);
		this.notifier = new BlockNotifier(this);
		this.requester = new BlockRequester(this);
	};

	render() {
		const display = this._is("flexible") ? "flex" : undefined;
		const direction = this._is("horizontal") ? "row" : "column";
		const wrap = this._is("wrap") ? "wrap" : "nowrap";
		const justify = this._justifyContent();
		const alignItems = this._alignContent();
		const spacing = this._horizontalSpacing();
		const isItem = this._widthDefined() && spacing === undefined;
		const xs = this._isRelativeWidth() ? this._relativeWidth() : undefined;
		const isVerticalSpacing = this._isVerticalSpacing();

		return (
			<Grid style={this.style()}
				  item={isItem}
				  xs={isItem ? xs : undefined}
				  display={display}
				  container={!isItem}
				  direction={!isItem ? direction : undefined}
				  wrap={!isItem ? wrap : undefined}
				  spacing={!isItem ? spacing : undefined}
				  justify={justify}
				  alignItems={alignItems}>
				{React.Children.map(this.props.children, (child, i) => {
					return isVerticalSpacing ? <div style={{paddingBottom: this.props.spacing + "px"}}>{child}</div> : child;
				})}
			</Grid>
		);
	};

	style() {
		var result = super.style();
		if (result == null) result = {};
		if (this.props.margin != null) result.margin = this.props.margin;
		if (this._isAbsoluteWidth()) result.width = this.props.width;
		if (this._isAbsoluteHeight()) result.height = this.props.height;
		return result;
	};

	_is = (layout) => {
		if (this.props.layout == null) return false;
		return this.props.layout.indexOf(layout) !== -1;
	}

	_justifyContent = () => {
		return this._justify("horizontal", "vertical");
	};

	_alignContent = () => {
		return this._justify("vertical", "horizontal");
	};

	_isVerticalSpacing = () => {
		return this.props.spacing != null && this._is("vertical");
	};

	_horizontalSpacing = () => {
		var spacing = this.props.spacing != null ? parseInt(this.props.spacing) : undefined;
		return this._is("horizontal") && spacing != null ? spacing : undefined;
	};

	_justify = (justify1, justify2) => {
		if (this._is(justify1)) {
			if (this._is("center")) return "center";
			else if (this._is("startjustified")) return "flex-start";
			else if (this._is("endjustified")) return "flex-end";
		}
		else if (this._is(justify2) && this._is("centerjustified"))
			return "center";

		return undefined;
	};

	_isRelativeWidth = () => {
		return this._widthDefined() && this.props.width.indexOf("%") != -1;
	};

	_isAbsoluteWidth = () => {
		return this._widthDefined() && this.props.width.indexOf("px") != -1;
	};

	_isAbsoluteHeight = () => {
		return this._heightDefined() && this.props.width.indexOf("px") != -1;
	};

	_relativeWidth = () => {
		const width = parseInt(this.props.width.replace("px", "").replace("%", ""));
		const absolute = this.props.width.indexOf("px") != -1;
		return absolute ? undefined : Math.round(12*width/100);
	};

	_widthDefined = () => {
		return this.props.width != null && this.props.width.indexOf("-1") === -1;
	};

	_heightDefined = () => {
		return this.props.height != null && this.props.height.indexOf("-1") === -1;
	}
}

export default withStyles(styles, { withTheme: true })(Block);