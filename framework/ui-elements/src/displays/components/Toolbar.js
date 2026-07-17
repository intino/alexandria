import React from "react";
import {withStyles} from 'alexandria-ui-elements/src/util/muiStylesCompat';
import AbstractToolbar from "../../../gen/displays/components/AbstractToolbar";
import ToolbarNotifier from "../../../gen/displays/notifiers/ToolbarNotifier";
import ToolbarRequester from "../../../gen/displays/requesters/ToolbarRequester";
import classNames from "classnames";
import 'alexandria-ui-elements/res/styles/layout.css';
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";
import {fieldPalette} from "./FieldStyles";
import Theme from "app-elements/gen/Theme";
import {themeFormatValue} from "./ThemeTokens";

const styles = theme => ({
	toolbar : {
		display: "inline-flex",
		alignItems: "center",
		flexWrap: "wrap",
		gap: "10px",
		padding: "4px 10px",
		minHeight: "46px",
		width: "100%",
		boxSizing: "border-box",
		justifyContent: "flex-start",
		borderRadius: "14px",
		background: fieldPalette(theme).dark ? "rgba(15, 23, 42, 0.32)" : "rgba(248, 250, 252, 0.58)",
		border: fieldPalette(theme).dark ? "1px solid rgba(148, 163, 184, 0.08)" : "1px solid rgba(15, 23, 42, 0.04)",
		boxShadow: "none",
		color: fieldPalette(theme).textColor,
		backdropFilter: "blur(6px)",
	},
	operation : {
		display: "inline-flex",
		alignItems: "center",
		justifyContent: "center",
		minHeight: "34px",
		padding: "2px 2px",
		borderRadius: "10px",
		fontWeight: "inherit",
		color: fieldPalette(theme).textColor,
		"& .MuiIconButton-root": {
			color: fieldPalette(theme).dark ? "rgba(226, 232, 240, 0.9)" : undefined,
			borderRadius: "10px",
			padding: "6px",
		},
		"& .MuiButton-root, & .MuiTypography-root, & a": {
			color: "inherit",
		},
		"& .MuiButton-root": {
			minHeight: "34px",
			padding: "4px 12px",
			borderRadius: "11px",
			background: fieldPalette(theme).dark ? "rgba(255,255,255,0.02)" : "rgba(255,255,255,0.72)",
			border: fieldPalette(theme).dark ? "1px solid rgba(148,163,184,0.10)" : "1px solid rgba(15,23,42,0.06)",
			textTransform: "uppercase",
			letterSpacing: "0.02em",
			boxShadow: "none",
			transition: "background-color 120ms ease, border-color 120ms ease, color 120ms ease",
		},
		"& .MuiButton-root:hover": {
			background: fieldPalette(theme).dark ? "rgba(144,202,249,0.08)" : "rgba(25,118,210,0.05)",
			border: fieldPalette(theme).dark ? "1px solid rgba(144,202,249,0.18)" : "1px solid rgba(25,118,210,0.12)",
			boxShadow: "none",
		},
		"& .MuiButton-root.Mui-disabled": {
			background: fieldPalette(theme).dark ? "rgba(15,23,42,0.18)" : "rgba(248,250,252,0.52)",
			border: fieldPalette(theme).dark ? "1px solid rgba(71,85,105,0.24)" : "1px solid rgba(148,163,184,0.22)",
			color: fieldPalette(theme).dark ? "rgba(148,163,184,0.46)" : "rgba(100,116,139,0.52)",
			opacity: 1,
			cursor: "not-allowed",
			pointerEvents: "auto",
		},
		"& .MuiButton-root.Mui-disabled:hover": {
			background: fieldPalette(theme).dark ? "rgba(15,23,42,0.18)" : "rgba(248,250,252,0.52)",
			border: fieldPalette(theme).dark ? "1px solid rgba(71,85,105,0.24)" : "1px solid rgba(148,163,184,0.22)",
			boxShadow: "none",
		},
		"& .MuiButton-root.MuiButton-contained": {
			background: fieldPalette(theme).dark ? "rgba(144,202,249,0.16)" : "rgba(25,118,210,0.10)",
			border: fieldPalette(theme).dark ? "1px solid rgba(144,202,249,0.28)" : "1px solid rgba(25,118,210,0.18)",
		},
		"& .MuiButton-root.MuiButton-contained:hover": {
			background: fieldPalette(theme).dark ? "rgba(144,202,249,0.22)" : "rgba(25,118,210,0.14)",
			border: fieldPalette(theme).dark ? "1px solid rgba(144,202,249,0.40)" : "1px solid rgba(25,118,210,0.24)",
		},
		"& .MuiButton-root.MuiButton-outlined": {
			background: "transparent",
			border: fieldPalette(theme).dark ? "1px solid rgba(148,163,184,0.18)" : "1px solid rgba(25,118,210,0.14)",
		},
		"& .MuiButton-root.MuiButton-text": {
			background: "transparent",
			border: "1px solid transparent",
		},
		"& .MuiIconButton-root.Mui-disabled": {
			color: fieldPalette(theme).dark ? "rgba(148,163,184,0.42)" : "rgba(100,116,139,0.48)",
			opacity: 1,
			cursor: "not-allowed",
			pointerEvents: "auto",
		},
	}
});

class Toolbar extends AbstractToolbar {

	constructor(props) {
		super(props);
		this.notifier = new ToolbarNotifier(this);
		this.requester = new ToolbarRequester(this);
	};

	render() {
	    if (!this.state.visible) return (<React.Fragment/>);
		const { classes } = this.props;
		const theme = Theme.get();
		const palette = fieldPalette(theme);
		const inlineStyle = this.style() || {};
		const hasBackground = inlineStyle.background != null || inlineStyle.backgroundColor != null;
		const themedToolbarStyle = !hasBackground ? {
			background: themeFormatValue(theme, "filledNoAir", "background", palette.dark ? "rgba(15, 23, 42, 0.72)" : "rgba(248, 250, 252, 0.92)"),
			border: palette.dark ? "1px solid rgba(148, 163, 184, 0.12)" : "1px solid rgba(15, 23, 42, 0.06)",
			color: palette.textColor,
		} : {};
		return (<div className={classNames("layout horizontal center", classes.toolbar)} style={{...inlineStyle, ...themedToolbarStyle}}>{this.props.children}</div>);
	};

}

export default withStyles(styles, { withTheme: true })(Toolbar);
DisplayFactory.register("Toolbar", withStyles(styles, { withTheme: true })(Toolbar));
