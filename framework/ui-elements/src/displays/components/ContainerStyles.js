export const containerPalette = (theme) => {
	const dark = theme.palette.mode === "dark";
	return {
		dark,
		surface: dark ? "#0c1624" : "#ffffff",
		surfaceMuted: dark ? "rgba(17,28,44,0.88)" : "#f8fbff",
		border: dark ? "rgba(125,154,192,0.22)" : "rgba(15,23,42,0.08)",
		headerBorder: dark ? "rgba(125,154,192,0.16)" : "rgba(15,23,42,0.06)",
		shadow: dark
			? "0 28px 64px rgba(2,6,23,0.58), 0 8px 24px rgba(8,18,36,0.36)"
			: "0 20px 48px rgba(15,23,42,0.14), 0 2px 10px rgba(15,23,42,0.06)",
		backdrop: dark ? "rgba(15,23,42,0.28)" : "rgba(15,23,42,0.18)",
		title: dark ? "rgba(244,248,255,0.98)" : "rgba(15,23,42,0.92)",
		text: dark ? "rgba(226,232,240,0.92)" : "rgba(15,23,42,0.72)",
	};
};

export const paperSurfaceStyles = (theme) => {
	const palette = containerPalette(theme);
	return {
		background: palette.surface,
		border: `1px solid ${palette.border}`,
		borderRadius: "24px",
		boxShadow: palette.shadow,
		overflow: "hidden",
	};
};

export const dialogPaperStyles = (theme) => {
	return {
		...paperSurfaceStyles(theme),
		backdropFilter: "blur(14px)",
	};
};
