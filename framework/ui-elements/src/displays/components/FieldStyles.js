import {syncThemeCssVariables, themeFormatValue} from "./ThemeTokens";

const normalizeFieldRadius = (value) => {
	if (value == null) return "16px";
	const numeric = parseFloat(value);
	if (Number.isNaN(numeric)) return value;
	return numeric < 12 ? "16px" : value;
};

export const fieldPalette = (theme) => {
	const dark = theme.palette.mode === "dark";
	syncThemeCssVariables(theme);
	const borderRadius = normalizeFieldRadius(themeFormatValue(theme, "whiteBackground", "borderRadius", "16px"));
	return {
		dark,
		background: "var(--alex-field-bg)",
		hoverBackground: "var(--alex-field-bg-hover)",
		focusBackground: "var(--alex-field-bg-focus)",
		borderColor: dark ? "rgba(148,163,184,0.32)" : "rgba(59,130,246,0.22)",
		hoverBorderColor: dark ? "rgba(191,219,254,0.46)" : "rgba(59,130,246,0.34)",
		focusColor: dark ? theme.palette.primary.light : theme.palette.primary.main,
		textColor: dark ? "rgba(255,255,255,0.92)" : "rgba(15,23,42,0.92)",
		placeholderColor: dark ? "rgba(226,232,240,0.5)" : "rgba(15,23,42,0.46)",
		disabledBackground: dark ? "rgba(15,23,42,0.52)" : "rgba(15,23,42,0.03)",
		disabledText: dark ? "rgba(255,255,255,0.92)" : "rgba(15,23,42,0.92)",
		focusRing: dark ? "rgba(96,165,250,0.22)" : "rgba(25,118,210,0.14)",
		shadow: dark ? "none" : "inset 0 1px 0 rgba(255,255,255,0.75)",
		borderRadius,
	};
};

export const outlinedFieldStyles = (theme) => {
	const {
		dark,
		background,
		hoverBackground,
		focusBackground,
		borderColor,
		hoverBorderColor,
		focusColor,
		textColor,
		placeholderColor,
		disabledBackground,
		disabledText,
		focusRing,
		shadow,
		borderRadius
	} = fieldPalette(theme);

	return {
		width: "100%",
		"& .MuiInputLabel-root": {
			color: dark ? "rgba(255,255,255,0.72)" : "rgba(15,23,42,0.62)",
			fontWeight: 500,
			letterSpacing: "0.01em",
		},
		"& .MuiInputLabel-root.Mui-focused": {
			color: focusColor,
		},
		"& .MuiInputBase-root": {
			borderRadius,
			backgroundColor: background,
			color: textColor,
			minHeight: "52px",
			boxShadow: shadow,
			transition: "background-color 160ms ease, box-shadow 160ms ease, border-color 160ms ease",
		},
		"& .MuiInputBase-root:not(.Mui-disabled)": {
			cursor: "text",
		},
		"& .MuiInputBase-root:hover": {
			backgroundColor: hoverBackground,
		},
		"& .MuiInputBase-root.Mui-disabled:hover": {
			backgroundColor: disabledBackground,
		},
		"& .MuiInputBase-root.Mui-focused": {
			backgroundColor: focusBackground,
			boxShadow: `0 0 0 4px ${focusRing}`,
		},
		"& .MuiInputBase-root.Mui-disabled": {
			backgroundColor: disabledBackground,
			boxShadow: "none",
		},
		"& .MuiOutlinedInput-notchedOutline": {
			borderColor: borderColor,
			borderWidth: "1px",
			borderRadius,
		},
		"& .MuiInputBase-root:hover .MuiOutlinedInput-notchedOutline": {
			borderColor: hoverBorderColor,
		},
		"& .MuiInputBase-root.Mui-disabled:hover .MuiOutlinedInput-notchedOutline": {
			borderColor: dark ? "rgba(255,255,255,0.1)" : "rgba(15,23,42,0.1)",
		},
		"& .MuiInputBase-root.Mui-focused .MuiOutlinedInput-notchedOutline": {
			borderColor: focusColor,
			borderWidth: "1px",
		},
		"& .MuiInputBase-root.Mui-disabled .MuiOutlinedInput-notchedOutline": {
			borderColor: dark ? "rgba(255,255,255,0.1)" : "rgba(15,23,42,0.1)",
		},
		"& .MuiOutlinedInput-input": {
			minHeight: "28px",
			paddingTop: "11px",
			paddingBottom: "11px",
			color: textColor,
		},
		"& .MuiOutlinedInput-input::placeholder": {
			color: placeholderColor,
			opacity: 1,
		},
		"& .MuiInputBase-input.Mui-disabled": {
			WebkitTextFillColor: disabledText,
		},
		"& .MuiInputAdornment-root": {
			color: dark ? "rgba(255,255,255,0.7)" : "rgba(15,23,42,0.62)",
		},
		"& .MuiFormHelperText-root": {
			marginLeft: "4px",
			marginRight: "4px",
			marginTop: "6px",
		},
		"& .MuiOutlinedInput-multiline": {
			padding: "6px 0",
			minHeight: "auto",
		},
	};
};

export const errorFieldStyles = (theme) => ({
	"& .MuiInputLabel-root": {
		color: theme.palette.mode === "dark" ? "#ffb4ab" : "#c62828",
	},
	"& .MuiInputLabel-root.Mui-focused": {
		color: theme.palette.mode === "dark" ? "#ffb4ab" : "#c62828",
	},
	"& .MuiOutlinedInput-notchedOutline": {
		borderColor: theme.palette.mode === "dark" ? "#ff8a80" : "#e57373",
		borderWidth: "1px",
	},
	"& .MuiInputBase-root:hover .MuiOutlinedInput-notchedOutline": {
		borderColor: theme.palette.mode === "dark" ? "#ff8a80" : "#d32f2f",
	},
	"& .MuiInputBase-root.Mui-focused .MuiOutlinedInput-notchedOutline": {
		borderColor: theme.palette.mode === "dark" ? "#ff8a80" : "#d32f2f",
		borderWidth: "1px",
	},
	"& .MuiInputBase-root:not(.Mui-focused) .MuiInputBase-input": {
		color: "transparent",
		WebkitTextFillColor: "transparent",
	},
	"& .MuiInputBase-root:not(.Mui-focused) .MuiInputBase-input::placeholder": {
		color: "transparent",
	},
	"& .MuiInputBase-root:not(.Mui-focused) .MuiInputAdornment-root": {
		opacity: 0,
	},
	"& .MuiInputBase-root:not(.Mui-focused) .MuiPickersSectionList-section": {
		color: "transparent",
	},
	"& .MuiInputBase-root:not(.Mui-focused) .MuiPickersInputBase-sectionsContainer": {
		color: "transparent",
	},
});

export const outlinedSurfaceStyles = (theme) => {
	const { dark, background, hoverBackground, borderColor, hoverBorderColor, focusColor, focusRing, shadow, borderRadius } = fieldPalette(theme);
	return {
		borderRadius,
		border: `1px solid ${borderColor}`,
		background: background,
		boxShadow: shadow,
		transition: "background-color 160ms ease, box-shadow 160ms ease, border-color 160ms ease",
		cursor: "pointer",
		"&:hover": {
			background: hoverBackground,
			borderColor: hoverBorderColor,
		},
		"&.readonly:hover": {
			background: background,
			borderColor: borderColor,
			boxShadow: shadow,
		},
		"&:focus-within": {
			background: dark ? "rgba(144,202,249,0.12)" : "#e3f2fd",
			borderColor: focusColor,
			boxShadow: `0 0 0 4px ${focusRing}`,
		},
	};
};

export const fieldErrorStyles = (theme, overrides = {}) => ({
	top: "50%",
	left: "16px",
	right: "12px",
	bottom: "auto",
	color: theme.palette.mode === "dark" ? "white" : "#c62828",
	position: "absolute",
	transform: "translateY(-50%)",
	background: "transparent",
	border: "none",
	padding: "0",
	borderRadius: "0",
	fontSize: "11pt",
	fontWeight: 400,
	minHeight: "auto",
	display: "flex",
	alignItems: "center",
	lineHeight: 1.2,
	zIndex: 1,
	overflow: "hidden",
	whiteSpace: "nowrap",
	textOverflow: "ellipsis",
	pointerEvents: "none",
	...overrides,
});
