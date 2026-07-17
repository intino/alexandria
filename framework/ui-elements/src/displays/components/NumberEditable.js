import React from "react";
import {NumericFormat} from "react-number-format";
import {withStyles} from 'alexandria-ui-elements/src/util/muiStylesCompat';
import AbstractNumberEditable from "../../../gen/displays/components/AbstractNumberEditable";
import NumberEditableNotifier from "../../../gen/displays/notifiers/NumberEditableNotifier";
import NumberEditableRequester from "../../../gen/displays/requesters/NumberEditableRequester";
import TextField from '@mui/material/TextField';
import InputAdornment from '@mui/material/InputAdornment';
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";
import {withSnackbar} from "alexandria-ui-elements/src/util/notistackCompat";
import Delayer from '../../util/Delayer';
import 'alexandria-ui-elements/res/styles/components/fields.css';
import classnames from 'classnames';
import Theme from "app-elements/gen/Theme";
import {errorFieldStyles, fieldErrorStyles, outlinedFieldStyles} from "./FieldStyles";

const styles = theme => ({
	default : outlinedFieldStyles(theme),
	errorField: errorFieldStyles(theme),
	input: {
		'&[type=number]': {
			'-moz-appearance': 'textfield',
		},
		'&::-webkit-outer-spin-button': {
			'-webkit-appearance': 'none',
			margin: 0,
		},
		'&::-webkit-inner-spin-button': {
			'-webkit-appearance': 'none',
			margin: 0,
		},
	},
	error : fieldErrorStyles(theme)
});

class NumberEditable extends AbstractNumberEditable {

	constructor(props) {
		super(props);
		this.notifier = new NumberEditableNotifier(this);
		this.requester = new NumberEditableRequester(this);
		this.inputRef = React.createRef();
		this.state = {
			...this.state,
			readonly: this.props.readonly,
			prefix: this.props.prefix,
			suffix: this.props.suffix,
			min : this.props.min !== -1 ? this.props.min : undefined,
			max : this.props.max !== -1 ? this.props.min : undefined,
		};
	};

	handleChange(e) {
		const value = e.target.value;
		this.setState({ value: value !== "" ? this._number(value) : "0" });
		Delayer.execute(this, () => this.requester.notifyChange(value !== "" ? this._number(value) : "0"), 500);
	};

	_number = (value) => {
		return value.replace(/\./g, "").replace(",", ".");
	};

	handleFocus(e) {
		const element = document.getElementById(this.props.id + "-error");
		if (element != null) element.style.display = "none";
	};

	handleBlur(e) {
		const element = document.getElementById(this.props.id + "-error");
		if (element != null) element.style.display = "block";
	}

	render() {
		if (!this.state.visible) return (<React.Fragment/>);

		const { classes } = this.props;
			const label = this.props.label !== "" ? this.translate(this.props.label) : undefined;
			const error = this.state.error;
			const shrink = error != null ? true : (this.props.shrink !== null ? this.props.shrink : undefined);
			const value = this.state.value != null ? this.state.value : (this.state.min !== -1 ? this.state.min : 0);
			const { thousandSeparator, decimalSeparator } = this.separators();
			const theme = Theme.get();
			const isDark = theme != null && theme.palette != null && theme.palette.mode === "dark";
			const fieldThemeClass = isDark ? "dark" : undefined;

			return (
				<div style={{position:"relative"}} className={fieldThemeClass}>
				{(!this.state.readonly && error != null) && <div id={this.props.id + "-error"} className={classes.error} style={this._errorStyle()}>{error}</div>}
				<NumericFormat
					value={value}
					customInput={TextField}
					thousandSeparator={thousandSeparator}
					decimalSeparator={decimalSeparator}
					decimalScale={this.props.decimals != null ? this.props.decimals : 0}
					format={this.variant("body1")} style={this.style()} className={classnames(classes.default, error != null ? classes.errorField : undefined, "number-editable")} label={label} type="text"
					onChange={this.handleChange.bind(this)} autoFocus={this.props.focused}
					autoComplete="off" disabled={this.state.readonly} size="Small" variant="outlined"
					inputRef={this.inputRef}
					onFocus={this.handleFocus.bind(this)}
					onBlur={this.handleBlur.bind(this)}
					slotProps={{
						inputLabel: {
							shrink: shrink
						},
						htmlInput: {
							min: this.state.min !== -1 ? this.state.min : undefined,
							max: this.state.max !== -1 ? this.state.max : undefined,
							step: this.props.step !== -1 ? this.props.step : undefined,
							className: classes.input
						},
						input: {
							style: { borderRadius: "16px" },
							readOnly: this.state.readonly,
							startAdornment: this.state.prefix !== undefined ? <InputAdornment position="start">{this.translate(this.state.prefix)}</InputAdornment> : undefined,
							endAdornment: this.state.suffix !== undefined ? <InputAdornment position="end">{this.translate(this.state.suffix)}</InputAdornment> : undefined
						}
					}}>
				</NumericFormat>
			</div>
		);
	};

    separators = () => {
		const language = window.Application.configuration.language;
        const parts = new Intl.NumberFormat(language).formatToParts(1234567.89);
        return {
            thousandSeparator: parts.find(p => p.type === 'group')?.value || null,
            decimalSeparator: parts.find(p => p.type === 'decimal')?.value || null
        };
    }

	refresh = (value) => {
		this.setState({ "value": value != null ? value : "" });
	};

	refreshReadonly = (readonly) => {
		this.setState({ readonly });
	};

	refreshFocused = (focused) => {
		if (this.inputRef == null || this.inputRef.current == null) return;
		window.setTimeout(() => {
			if (this.state.readonly) this.inputRef.current.scrollIntoView();
			else this.inputRef.current.focus();
		}, 100);
	};

	refreshRange = (range) => {
		this.setState({ min: range.min, max: range.max });
	};

	_errorStyle = () => {
		return {};
	};
}

export default withStyles(styles, { withTheme: true })(withSnackbar(NumberEditable));
DisplayFactory.register("NumberEditable", withStyles(styles, { withTheme: true })(withSnackbar(NumberEditable)));
