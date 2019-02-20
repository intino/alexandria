import React from "react";
import Grid from "@material-ui/core/Grid";
import AbstractBlock from "../../../gen/displays/components/AbstractBlock";
import BlockNotifier from "../../../gen/displays/notifiers/BlockNotifier";
import BlockRequester from "../../../gen/displays/requesters/BlockRequester";

export default class Block extends AbstractBlock {

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
		const widthDefined = this.props.width != null && this.props.width !== "";
		const xs = widthDefined ? parseInt(this.props.width) : undefined;

		return (
			<Grid item={widthDefined} container={!widthDefined}
				  xs={widthDefined ? xs : undefined}
				  display={display}
				  direction={!widthDefined ? direction : undefined}
				  wrap={!widthDefined ? wrap : undefined}
				  justify={justify}
				  alignItems={alignItems}>{this.props.children}</Grid>
		);
	};

	_is = (layout) => {
		return this.props.layout.indexOf(layout) !== -1;
	}

	_justifyContent = () => {
		return this._justify("horizontal", "vertical");
	};

	_alignContent = () => {
		return this._justify("vertical", "horizontal");
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
}