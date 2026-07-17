const currentFormats = (theme) => {
	if (theme == null) return {};
	const dark = theme.palette != null && theme.palette.mode === "dark";
	if (dark && theme.darkFormats != null) return theme.darkFormats;
	return theme.formats || {};
};

export const themeFormat = (theme, name) => {
	const formats = currentFormats(theme);
	return name != null ? formats[name] : undefined;
};

export const themeFormatValue = (theme, name, property, fallback) => {
	const format = themeFormat(theme, name);
	if (format == null || format[property] == null) return fallback;
	return format[property];
};

export const linkPalette = (theme) => {
	const dark = theme != null && theme.palette != null && theme.palette.mode === "dark";
	const primary = theme != null && theme.palette != null ? theme.palette.primary || {} : {};
	return {
		color: primary.main || (dark ? "#7dd3fc" : "#0f6cbd"),
		hoverColor: dark
			? (primary.light || primary.main || "#bae6fd")
			: (primary.dark || primary.main || "#0b5cab"),
	};
};

const setCssVariable = (name, value) => {
	if (typeof document === "undefined" || document.documentElement == null || value == null) return;
	document.documentElement.style.setProperty(name, value);
};

const normalizeFieldRadius = (value) => {
	if (value == null) return "16px";
	const numeric = parseFloat(value);
	if (Number.isNaN(numeric)) return value;
	return numeric < 12 ? "16px" : value;
};

export const syncThemeCssVariables = (theme) => {
	if (theme == null || theme.palette == null) return;
	const dark = theme.palette.mode === "dark";
	const fieldBackground = themeFormatValue(theme, "whiteBackground", "background", dark ? "rgba(20,27,36,0.96)" : "#f8fbff");
	const collectionBackground = themeFormatValue(theme, "filled", "background", dark ? "rgba(15, 23, 34, 0.98)" : "rgba(248, 251, 255, 0.72)");
	const borderRadius = normalizeFieldRadius(themeFormatValue(theme, "whiteBackground", "borderRadius", "16px"));
	setCssVariable("--alex-field-bg", fieldBackground);
	setCssVariable("--alex-collection-bg", collectionBackground);
	setCssVariable("--alex-collection-header-bg", collectionBackground);
	setCssVariable("--alex-field-radius", borderRadius);
};
