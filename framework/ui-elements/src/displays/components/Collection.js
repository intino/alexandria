import React from "react";
import AbstractCollection from "../../../gen/displays/components/AbstractCollection";
import CollectionBehavior from "./behaviors/CollectionBehavior";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";
import {fieldPalette} from "./FieldStyles";
import {syncThemeCssVariables, themeFormatValue} from "./ThemeTokens";

export const collectionPalette = (theme) => {
	const dark = theme.palette.mode === "dark";
	syncThemeCssVariables(theme);
	const collectionBackground = "none"; //themeFormatValue(theme, "filled", "background", dark ? "rgba(10, 15, 24, 0.96)" : "rgba(248,251,255,0.72)");
	const collectionHeaderBackground = themeFormatValue(theme, "filledNoAir", "background", collectionBackground);
	return {
		viewportBackground: collectionBackground,
		headerBackground: collectionHeaderBackground,
		rowHoverBackground: dark ? "rgba(30, 41, 59, 0.92)" : "rgba(15,23,42,0.05)",
		borderColor: dark ? "rgba(148, 163, 184, 0.14)" : "rgba(15,23,42,0.08)",
		rowBorderColor: dark ? "rgba(71, 85, 105, 0.34)" : "rgba(15,23,42,0.06)",
		scrollThumb: dark ? "rgba(100, 116, 139, 0.34)" : "rgba(15,23,42,0.08)",
	};
};

export const CollectionStyles = theme => ({
	collectionViewport: {
		borderRadius: "18px",
		minHeight: 0,
	},
	scrolling: {
		background: collectionPalette(theme).scrollThumb,
		height: "50%",
		borderRadius: "14px"
	},
	selectable : {
		paddingLeft: "42px !important"
	},
	selected : {
		background: "blue"
	},
	selector : {
		position: "absolute",
		left: "12px",
		top: "50%",
		transform: "translateY(-50%)",
		opacity: 0,
		pointerEvents: "auto",
		transition: "opacity 120ms ease-in-out, color 120ms ease-in-out",
		color: fieldPalette(theme).focusColor,
	},
	selecting : {
		"& $selector" : {
			opacity: 1,
			pointerEvents: "auto"
		}
	}
});

export default class Collection extends AbstractCollection {

	constructor(props) {
		super(props);
		this.behavior = new CollectionBehavior(this);
		this.container = React.createRef();
		this.onClearContainer = this.clearSelection.bind(this);
		this.state = {
			multiSelection: this.props.selection != null && this.props.selection === "multiple",
			itemCount: 20,
			pageSize: 20,
			page: 0,
			...this.state
		};
	};

	setup = (info) => {
		this.setState({ itemCount : info.itemCount, pageSize: info.pageSize });
	};

	refresh = () => {
		this.behavior.refresh();
	};

	refreshItemCount = (itemCount) => {
		this.setState({ itemCount });
	};

	refreshSelection = (selection) => {
		this.behavior.refreshSelection(selection);
	};

	refreshAllowMultiSelection = (value) => {
		this.setState({ multiSelection: value });
	};

	allowMultiSelection = () => {
		if (this.state.multiSelection != null) return this.state.multiSelection;
		return this.props.selection != null && this.props.selection === "multiple";
	};

	clearSelection = () => {
		if (this.allowMultiSelection()) this.selection = [];
	};
}

DisplayFactory.register("Collection", Collection);
