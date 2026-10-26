import React from "react";
import AbstractTab from "../../../gen/displays/components/AbstractTab";
import TabNotifier from "../../../gen/displays/notifiers/TabNotifier";
import TabRequester from "../../../gen/displays/requesters/TabRequester";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

export const TabContent = React.forwardRef(({children, highlighted, theme, style, surfaceInset, ...props}, ref) => {
	const primary = theme != null && theme.palette != null && theme.palette.primary != null ? theme.palette.primary.main : "#1976d2";
	const isDark = theme != null && theme.palette != null && theme.palette.mode === "dark";
	const highlightedText = !isDark && theme != null && theme.palette != null && theme.palette.primary != null && theme.palette.primary.dark != null ? theme.palette.primary.dark : primary;
	return (
		<span ref={ref} {...props} style={{
			display: "inline-flex",
			position: "relative",
			alignItems: "center",
			justifyContent: "center",
			color: highlighted ? highlightedText : undefined,
			transition: "color 160ms ease",
			...(style || {})
		}}>
			<span aria-hidden="true" style={{
				position: "absolute",
				inset: surfaceInset || "-9px -18px",
				borderRadius: "16px",
				background: isDark ? `color-mix(in srgb, ${primary} 28%, #1e293b)` : `color-mix(in srgb, ${primary} 10%, white)`,
				opacity: highlighted ? 1 : 0,
				pointerEvents: "none",
				transition: "opacity 160ms ease"
			}}/>
			<span style={{position: "relative"}}>{children}</span>
		</span>
	);
});

export default class Tab extends AbstractTab {

	constructor(props) {
		super(props);
		this.notifier = new TabNotifier(this);
		this.requester = new TabRequester(this);
	};

	render() {
		return (
			<React.Fragment>
			</React.Fragment>
		);
	};

}

DisplayFactory.register("Tab", Tab);
