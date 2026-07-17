import {
    createGenerateClassName,
    createStyles,
    getThemeProps,
    jssPreset,
    makeStyles,
    ServerStyleSheets,
    styled,
    StylesProvider,
    ThemeProvider,
    useTheme,
    withStyles as muiWithStyles,
    withTheme
} from "@mui/styles/index.js";
import {createTheme} from "@mui/material/styles";

const enrichTheme = (theme) => {
	const enrichedTheme = theme || {};
	const palette = enrichedTheme.palette || {};
	const mode = palette.mode || palette.type || (typeof enrichedTheme.isDark === "function" && enrichedTheme.isDark() ? "dark" : "light");
	enrichedTheme.palette = { ...palette, mode };
	if (typeof enrichedTheme.isDark !== "function") enrichedTheme.isDark = () => enrichedTheme.palette.mode === "dark";
	if (typeof enrichedTheme.isLight !== "function") enrichedTheme.isLight = () => enrichedTheme.palette.mode !== "dark";
	return enrichedTheme;
};

const wrapStylesCreator = (stylesOrCreator) => {
	if (typeof stylesOrCreator !== "function") return stylesOrCreator;
	return (theme) => stylesOrCreator(enrichTheme(theme));
};

const defaultTheme = enrichTheme(createTheme());

const withStyles = (stylesOrCreator, options) => muiWithStyles(wrapStylesCreator(stylesOrCreator), {
	defaultTheme,
	...(options || {})
});

export {
	createGenerateClassName,
	createStyles,
	getThemeProps,
	jssPreset,
	makeStyles,
	ServerStyleSheets,
	styled,
	StylesProvider,
	ThemeProvider,
	useTheme,
	withStyles,
	withTheme
};
