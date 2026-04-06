import React from "react";
import {NumericFormat} from "react-number-format";
import {withStyles} from '@material-ui/core/styles';
import AbstractNumberEditable from "../../../gen/displays/components/AbstractNumberEditable";
import NumberEditableNotifier from "../../../gen/displays/notifiers/NumberEditableNotifier";
import NumberEditableRequester from "../../../gen/displays/requesters/NumberEditableRequester";
import TextField from '@material-ui/core/TextField';
import InputAdornment from '@material-ui/core/InputAdornment';
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";
import {withSnackbar} from 'notistack';
import Delayer from '../../util/Delayer';
import 'alexandria-ui-elements/res/styles/components/fields.css';
import classnames from 'classnames';

const styles = theme => ({
	default : {
		width: "100%",
	},
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
	error : {
		top: '0px',
		left: '0px',
		color: '#e13939',
		width: 'calc(100% - 4px)',
		height: "calc(100% - 14px)",
		margin: '12px 2px',
		position: 'absolute',
		background: '#fdecec',
		padding: '14px 15px 0',
		borderRadius: '14px',
		fontSize: '12pt',
		zIndex: 1,
	}
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

	handleMouseEnter() {
		const element = document.getElementById(this.props.id + "-error");
		if (element != null) element.style.display = "none";
	};

	handleMouseLeave() {
		const element = document.getElementById(this.props.id + "-error");
		if (element != null && document.activeElement !== this.inputRef.current) element.style.display = "block";
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
		const value = this.state.value != null ? this.state.value : (this.state.min !== -1 ? this.state.min : 0);
		const { thousandSeparator, decimalSeparator } = this.separators();

		return (
			<div style={{position:"relative"}}>
				{(!this.state.readonly && error != null) && <div id={this.props.id + "-error"} className={classes.error} style={this._errorStyle()} onMouseEnter={this.handleMouseEnter.bind(this)}>{error}</div>}
				<NumericFormat
					value={value}
					customInput={TextField}
					thousandSeparator={thousandSeparator}
					decimalSeparator={decimalSeparator}
					decimalScale={this.props.decimals != null ? this.props.decimals : 0}
					format={this.variant("body1")} style={this.style()} className={classnames(classes.default, "number-editable")} label={label} type="text"
					onChange={this.handleChange.bind(this)} autoFocus={this.props.focused}
					onMouseLeave={this.handleMouseLeave.bind(this)}
					autoComplete="off" disabled={this.state.readonly} size="Small" variant="outlined"
					InputLabelProps={{ shrink: this.props.shrink !== null ? this.props.shrink : undefined }}
					inputRef={this.inputRef}
					onFocus={this.handleFocus.bind(this)}
					onBlur={this.handleBlur.bind(this)}
					inputProps={{
						min: this.state.min !== -1 ? this.state.min : undefined,
						max: this.state.max !== -1 ? this.state.max : undefined,
						step: this.props.step !== -1 ? this.props.step : undefined,
						className: classes.input
					}}
					InputProps={{
						readOnly: this.state.readonly,
						startAdornment: this.state.prefix !== undefined ? <InputAdornment position="start">{this.translate(this.state.prefix)}</InputAdornment> : undefined,
						endAdornment: this.state.suffix !== undefined ? <InputAdornment position="end">{this.translate(this.state.suffix)}</InputAdornment> : undefined
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
		this.setState({ "value": value });
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
		const style = this.style();
		let current = style.marginTop;
		if (!current) return { marginTop: "2px" };
		const match = current.match(/^([\d.]+)px$/);
		if (!match) return { marginTop: "2px" };
		let marginTop = parseFloat(match[1]);
		marginTop += 2;
		return { marginTop: marginTop + "px" }
	};
}

export default withStyles(styles, { withTheme: true })(withSnackbar(NumberEditable));
DisplayFactory.register("NumberEditable", withStyles(styles, { withTheme: true })(withSnackbar(NumberEditable)));