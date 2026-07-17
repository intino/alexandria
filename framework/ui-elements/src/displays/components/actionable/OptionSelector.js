import React from "react";
import {withStyles} from 'alexandria-ui-elements/src/util/muiStylesCompat';
import {FormControl, MenuItem, TextField} from "@mui/material";
import I18nComponent from "../../I18nComponent";
import Theme from "app-elements/gen/Theme";
import {fieldPalette, outlinedFieldStyles} from "../FieldStyles";

const styles = theme => ({
	options : {
		marginBottom: "20px",
		width: "100%"
	},
	select: outlinedFieldStyles(theme),
	menuPaper: {
		background: fieldPalette(theme).background,
		color: fieldPalette(theme).textColor,
		border: `1px solid ${fieldPalette(theme).borderColor}`,
		borderRadius: "16px",
		boxShadow: fieldPalette(theme).dark ? "0 16px 36px rgba(2,6,23,0.46)" : "0 12px 28px rgba(15,23,42,0.12)",
	},
	menuItem: {
		color: `${fieldPalette(theme).textColor} !important`,
		"&.Mui-selected": {
			background: `${fieldPalette(theme).focusColor} !important`,
			color: `${fieldPalette(theme).dark ? "#08111d" : "white"} !important`,
		},
		"&.Mui-selected:hover": {
			background: `${fieldPalette(theme).focusColor} !important`,
			color: `${fieldPalette(theme).dark ? "#08111d" : "white"} !important`,
		},
		"&:hover": {
			background: `${fieldPalette(theme).hoverBackground} !important`,
		},
	}
});

class OptionSelector extends I18nComponent {

	state = {
		option: this.props.options != null && this.props.options.length > 0 ? this.props.options[0] : ""
	};

	constructor(props) {
		super(props);
	};

	render = () => {
		const theme = Theme.get();
		const palette = fieldPalette(theme);
		const isDark = theme != null && theme.palette != null && theme.palette.mode === "dark";
		const { classes } = this.props;
		const options = this._options();
		if (options.length <= 1) return;
		return (<FormControl className={`${this.props.classes.options} export-selector ${isDark ? "dark" : ""}`}>
				<TextField
					select
					value={this.state.option}
					className={classes.select}
					variant="outlined"
					size="Small"
					onChange={this.handleOptionChange.bind(this)}
					slotProps={{
						input: {
							style: { borderRadius: "16px" }
						},
						select: {
							displayEmpty: true,
							MenuProps: isDark ? { PaperProps: { className: classes.menuPaper } } : undefined
						}
					}}
				>
					<MenuItem className={classes.menuItem} value="" disabled>{this.translate("Select an option")}</MenuItem>
					{options.map((option, i) => <MenuItem className={classes.menuItem} key={i} value={option}>{option}</MenuItem>)}
				</TextField>
			</FormControl>
		);
	};

	handleOptionChange(e) {
		if (this.props.onSelect) this.props.onSelect(e.target.value);
		this.setState({ option: e.target.value });
	};

	_options = () => {
		return this.props.options != null ? this.props.options : [];
	};

}

export default withStyles(styles, { withTheme: true })(OptionSelector);
