import React from "react";
import {withStyles} from "@material-ui/core/styles";
import AbstractTextEditable from "../../../gen/displays/components/AbstractTextEditable";
import TextEditableNotifier from "../../../gen/displays/notifiers/TextEditableNotifier";
import TextEditableRequester from "../../../gen/displays/requesters/TextEditableRequester";
import TextField from '@material-ui/core/TextField';
import InputAdornment from '@material-ui/core/InputAdornment';
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";
import InputMask from "react-input-mask";
import {withSnackbar} from 'notistack';
import Delayer from '../../util/Delayer';
import TextBehavior from "./behaviors/TextBehavior";
import Editor from 'react-simple-wysiwyg';
import 'alexandria-ui-elements/res/styles/components/fields.css';
import classnames from "classnames";

const styles = theme => ({
	default : {
		width: "100%"
	},
	root : {
		"& .MuiInputBase-root": {
			backgroundColor: "red",
		}
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
		Delayer.stop(this);
		this.requester.notifyBlur(this.state.value);
	};

	render() {
		if (!this.state.visible) return (<React.Fragment/>);
		const onFocus = this.handleFocus.bind(this);
		const onMouseLeave = this.handleMouseLeave.bind(this);
		const onBlur = this.handleBlur.bind(this);
		if (!this.state.readonly && this.state.pattern != null) return this.renderWithMask({onMouseLeave: onMouseLeave, onFocus: onFocus, onBlur: onBlur});
		return this.renderComponent({value: this.state.value, onMouseLeave: onMouseLeave, onFocus: onFocus, onBlur: onBlur, onChange: this.handleChange.bind(this), maxLength: this.props.maxLength != null ? this.props.maxLength : undefined});
	};

	renderWithMask = (props) => {
		const formatChars = this._formatChars();
		return (
			<InputMask {...props} mask={this.state.pattern.value} formatChars={formatChars}
					   value={this.state.value} onChange={this.handleChange.bind(this)} /*disabled={this.state.readonly}*/
					   alwaysShowMask={true} maskChar={this.state.pattern.maskCharacter}>
				{() => this.renderComponent()}
			</InputMask>
		);
	};

	renderComponent = (props) => {
		if (this.props.editionMode === "Rich") return this.renderRichEditor(props);
		return this.renderField(props);
	};

	renderRichEditor = (props) => {
		return (
			<div className={classnames("text-editable-rich-editor", this.state.readonly ? "readonly" : undefined)} style={this.style()}>
				<Editor {...props} containerProps={{ style: { ...this.style(), resize: 'vertical', height:'100%'/*, fontSize: '14pt'*/ } }}
						value={this.state.value} disabled={this.state.readonly}
						onChange={this.handleChange.bind(this)} />
			</div>
		);
	};

	renderField = (props) => {
		const { classes } = this.props;
		const label = this.props.label !== "" ? this.translate(this.props.label) : undefined;
		const placeholder = !this.state.readonly && this.props.placeholder !== "" ? this.translate(this.props.placeholder) : undefined;
		const type = this.props.type != null ? this.props.type : undefined;
		const error = this.state.error;

		return (
			<div style={{position:"relative"}}>
				{(!this.state.readonly && error != null) && <div ref={this.errorRef} id={this.props.id + "-error"} className={classes.error} onMouseEnter={this.handleMouseEnter.bind(this)}>{error}</div>}
				<TextField {...props} format={this.variant("body1")} style={this.style()} className={classes.default} label={label}
						   onKeyPress={this.handleKeypress.bind(this)} type={type} autoFocus={this.props.focused}
						   placeholder={placeholder} multiline={this._multiline()} rows={this._rowsCount()}
						   InputLabelProps={{ shrink: this.state.readonly ? true : this.props.shrink !== null ? this.props.shrink : undefined }}
						   disabled={this.state.readonly}
						   size="Small" variant="outlined"
						   inputRef={this.inputRef}
						   inputProps={{ style: {...this.style(), margin:'0'}, maxLength: props != null ? props.maxLength : undefined }}
						   InputProps={{
							   readOnly: this.state.readonly,
							   startAdornment: this.props.prefix !== undefined ? <InputAdornment position="start">{this.props.prefix}</InputAdornment> : undefined,
							   endAdornment: this.props.suffix !== undefined ? <InputAdornment position="end">{this.props.suffix}</InputAdornment> : undefined
						   }}></TextField>
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

}

export default withStyles(styles, { withTheme: true })(withSnackbar(TextEditable));
DisplayFactory.register("TextEditable", withStyles(styles, { withTheme: true })(withSnackbar(TextEditable)));