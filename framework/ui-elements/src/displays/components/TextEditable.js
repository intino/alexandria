import React from "react";
import {withStyles} from "alexandria-ui-elements/src/util/muiStylesCompat";
import AbstractTextEditable from "../../../gen/displays/components/AbstractTextEditable";
import TextEditableNotifier from "../../../gen/displays/notifiers/TextEditableNotifier";
import TextEditableRequester from "../../../gen/displays/requesters/TextEditableRequester";
import TextField from '@mui/material/TextField';
import InputAdornment from '@mui/material/InputAdornment';
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";
import {withSnackbar} from "alexandria-ui-elements/src/util/notistackCompat";
import {IMaskMixin} from "react-imask";
import Delayer from '../../util/Delayer';
import TextBehavior from "./behaviors/TextBehavior";
import Editor from 'react-simple-wysiwyg';
import 'alexandria-ui-elements/res/styles/components/fields.css';
import classnames from "classnames";
import Theme from "app-elements/gen/Theme";
import {errorFieldStyles, fieldErrorStyles, outlinedFieldStyles} from "./FieldStyles";

const styles = theme => ({
	default : outlinedFieldStyles(theme),
	error : fieldErrorStyles(theme),
	errorField: errorFieldStyles(theme)
});

const MaskedTextField = IMaskMixin(({ inputRef, externalInputRef, ...props }) => {
	const handleInputRef = (node) => {
		inputRef(node);
		if (externalInputRef == null) return;
		if (typeof externalInputRef === "function") externalInputRef(node);
		else externalInputRef.current = node;
	};

	return <TextField {...props} inputRef={handleInputRef} />;
});

class TextEditable extends AbstractTextEditable {

	constructor(props) {
		super(props);
		this.notifier = new TextEditableNotifier(this);
		this.requester = new TextEditableRequester(this);
		this.inputRef = React.createRef();
		this.state = {
			...this.state,
			value: '',
			pattern: null,
			readonly : this.props.readonly
		};
	};

	handleChange(e) {
		const value = TextBehavior.mode(e.target.value, this.props);
		this.setState({ value: value });
		if (this.timeout != null) window.clearTimeout(this.timeout);
		this.timeout = window.setTimeout(() => this.requester.notifyChange(value), 500);
	};

	handleKeypress(e) {
		this.requester.notifyKeyPress({ keyCode: e.key, value: TextBehavior.mode(e.target.value, this.props) });
		this.setState({ value: e.target.value });
	};

	handleFocus(e) {
		const element = document.getElementById(this.props.id + "-error");
		if (element != null) element.style.display = "none";
		this.requester.notifyFocus();
	};

	handleBlur(e) {
		const element = document.getElementById(this.props.id + "-error");
		if (element != null) element.style.display = "block";
		Delayer.stop(this);
		this.requester.notifyBlur(this.state.value);
	};

	render() {
		if (!this.state.visible) return (<React.Fragment/>);
		const onFocus = this.handleFocus.bind(this);
		const onBlur = this.handleBlur.bind(this);
		if (!this.state.readonly && this.state.pattern != null) return this.renderWithMask({onFocus: onFocus, onBlur: onBlur});
		return this.renderComponent({value: this.state.value, onFocus: onFocus, onBlur: onBlur, onChange: this.handleChange.bind(this), maxLength: this.props.maxLength != null ? this.props.maxLength : undefined});
	};

	renderWithMask = (props) => {
		if (this.props.editionMode === "Rich") return this.renderRichEditor(props);
		return this.renderMaskedField(props);
	};

	handleMaskedAccept = (value) => {
		const nextValue = TextBehavior.mode(value, this.props);
		this.setState({ value: nextValue });
		if (this.timeout != null) window.clearTimeout(this.timeout);
		this.timeout = window.setTimeout(() => this.requester.notifyChange(nextValue), 500);
	};

	renderComponent = (props) => {
		if (this.props.editionMode === "Rich") return this.renderRichEditor(props);
		return this.renderField(props);
	};

	renderRichEditor = (props) => {
		const theme = Theme.get();
		return (
			<div className={classnames("text-editable-rich-editor", this.state.readonly ? "readonly" : undefined, theme.palette.mode === "dark" ? "dark" : undefined)} style={this.style()}>
				<Editor {...props} containerProps={{ style: { ...this.style(), resize: 'vertical', height:'100%'/*, fontSize: '14pt'*/ } }}
						value={this.state.value} disabled={this.state.readonly}
						onChange={this.handleChange.bind(this)} />
			</div>
		);
	};

	renderField = (props) => {
		const { classes } = this.props;
		const label = this.props.label !== "" ? this.translate(this.props.label) : undefined;
		const type = this.props.type != null ? this.props.type : undefined;
		const error = this.state.error;
		const placeholder = !this.state.readonly && error == null && this.props.placeholder !== "" ? this.translate(this.props.placeholder) : undefined;
		const shrink = error != null ? true : (this.state.readonly ? true : this.props.shrink !== null ? this.props.shrink : undefined);
		const theme = Theme.get();

		return (
			<div style={{position:"relative",...this.style()}} className={theme.palette.mode === "dark" ? "dark" : undefined}>
				{(!this.state.readonly && error != null) && <div id={this.props.id + "-error"} className={classes.error}>{error}</div>}
				<TextField {...props} format={this.variant("body1")} className={classnames(classes.default, error != null ? classes.errorField : undefined)} label={label}
						   onKeyPress={this.handleKeypress.bind(this)} type={type} autoFocus={this.props.focused}
						   placeholder={placeholder} multiline={this._multiline()} rows={this._rowsCount()}
						   helperText={this.state.readonly ? undefined : this.props.helperText}
						   disabled={this.state.readonly}
						   size="Small" variant="outlined"
						   inputRef={this.inputRef}
						   slotProps={{
							   inputLabel: {
								   shrink: shrink
							   },
							   htmlInput: {
								   style: {...this.style(), margin:'0'},
								   maxLength: props != null ? props.maxLength : undefined
							   },
							   input: {
								   style: { borderRadius: "16px" },
								   readOnly: this.state.readonly,
								   startAdornment: this.props.prefix !== undefined ? <InputAdornment position="start">{this.props.prefix}</InputAdornment> : undefined,
								   endAdornment: this.props.suffix !== undefined ? <InputAdornment position="end">{this.props.suffix}</InputAdornment> : undefined
							   }
						   }}></TextField>
			</div>
		);
	};

	renderMaskedField = (props) => {
		const { classes } = this.props;
		const label = this.props.label !== "" ? this.translate(this.props.label) : undefined;
		const type = this.props.type != null ? this.props.type : undefined;
		const error = this.state.error;
		const placeholder = !this.state.readonly && error == null && this.props.placeholder !== "" ? this.translate(this.props.placeholder) : undefined;
		const shrink = error != null ? true : (this.state.readonly ? true : this.props.shrink !== null ? this.props.shrink : undefined);
		const theme = Theme.get();
		const definitions = this._maskDefinitions();

		return (
			<div style={{position:"relative",...this.style()}} className={theme.palette.mode === "dark" ? "dark" : undefined}>
				{(!this.state.readonly && error != null) && <div id={this.props.id + "-error"} className={classes.error}>{error}</div>}
				<MaskedTextField {...props} format={this.variant("body1")} className={classnames(classes.default, error != null ? classes.errorField : undefined)} label={label}
								onKeyPress={this.handleKeypress.bind(this)} type={type} autoFocus={this.props.focused}
								placeholder={placeholder} multiline={this._multiline()} rows={this._rowsCount()}
								helperText={this.state.readonly ? undefined : this.props.helperText}
								disabled={this.state.readonly}
								size="Small" variant="outlined"
								externalInputRef={this.inputRef}
								slotProps={{
									inputLabel: {
										shrink: shrink
									},
									htmlInput: {
										style: {...this.style(), margin:'0'},
										maxLength: props != null ? props.maxLength : undefined
									},
									input: {
										style: { borderRadius: "16px" },
										readOnly: this.state.readonly,
										startAdornment: this.props.prefix !== undefined ? <InputAdornment position="start">{this.props.prefix}</InputAdornment> : undefined,
										endAdornment: this.props.suffix !== undefined ? <InputAdornment position="end">{this.props.suffix}</InputAdornment> : undefined
									}
								}}
								mask={this.state.pattern.value}
								definitions={definitions}
								value={this.state.value}
								unmask={false}
								lazy={false}
								placeholderChar={this.state.pattern.maskCharacter != null ? this.state.pattern.maskCharacter : "_"}
								onAccept={this.handleMaskedAccept.bind(this)} />
			</div>
		);
	};

	_multiline = () => {
		const mode = this.props.editionMode;
		return mode != null && mode === "Raw";
	};

	_rowsCount = () => {
		const rows = this.props.rows;
		return rows != null ? rows : undefined;
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

	refreshPattern = (pattern) => {
		this.setState({pattern});
	};

	_formatChars = () => {
		const rules = this.state.pattern.rules;
		const result = {};
		for (let i=0; i<rules.length; i++) result[rules[i].name] = rules[i].value;
		return result;
	};

	_maskDefinitions = () => {
		const formatChars = this._formatChars();
		const definitions = {};
		for (const key in formatChars) definitions[key] = new RegExp(this._normalizeMaskRule(formatChars[key]));
		return definitions;
	};

	_normalizeMaskRule = (rule) => {
		if (rule == null) return rule;
		const parts = [];
		let current = "";
		let escaped = false;
		let inCharClass = false;

		for (let i = 0; i < rule.length; i++) {
			const char = rule[i];

			if (escaped) {
				current += char;
				escaped = false;
				continue;
			}

			if (char === "\\") {
				current += char;
				escaped = true;
				continue;
			}

			if (char === "[" && !inCharClass) {
				inCharClass = true;
				current += char;
				continue;
			}

			if (char === "]" && inCharClass) {
				inCharClass = false;
				current += char;
				continue;
			}

			if (char === "|" && !inCharClass) {
				parts.push(current);
				current = "";
				continue;
			}

			current += char;
		}

		parts.push(current);
		return parts.map(this._normalizeMaskRulePart).join("|");
	};

	_normalizeMaskRulePart = (part) => {
		if (part == null || part === "") return part;
		if (part.length !== 1) return part;
		if (/[+*?(){}]/.test(part)) return "\\" + part;
		return part;
	};

}

export default withStyles(styles, { withTheme: true })(withSnackbar(TextEditable));
DisplayFactory.register("TextEditable", withStyles(styles, { withTheme: true })(withSnackbar(TextEditable)));
