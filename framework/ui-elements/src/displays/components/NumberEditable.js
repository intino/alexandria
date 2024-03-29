import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractNumberEditable from "../../../gen/displays/components/AbstractNumberEditable";
import NumberEditableNotifier from "../../../gen/displays/notifiers/NumberEditableNotifier";
import NumberEditableRequester from "../../../gen/displays/requesters/NumberEditableRequester";
import TextField from '@material-ui/core/TextField';
import InputAdornment from '@material-ui/core/InputAdornment';
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";
import { withSnackbar } from 'notistack';
import Delayer from '../../util/Delayer';

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
            min : this.props.min != -1 ? this.props.min : undefined,
            max : this.props.max != -1 ? this.props.min : undefined,
        };
	};

	handleChange(e) {
		const value = e.target.value;
		this.setState({ value: value });
		Delayer.execute(this, () => this.requester.notifyChange(value !== "" ? value : "0"), 500);
	};

	render() {
	    if (!this.state.visible) return (<React.Fragment/>);

		const { classes } = this.props;
		const label = this.props.label !== "" ? this.translate(this.props.label) : undefined;
		const error = this.state.error;
		const value = this.state.value != null ? this.state.value : (this.state.min !== -1 ? this.state.min : 0);

		return (
			<TextField format={this.variant("body1")} style={this.style()} className={classes.default} label={label} type="number"
					   value={value} onChange={this.handleChange.bind(this)} /*disabled={this.state.readonly}*/ autoFocus={this.props.focused}
					   error={error != null} helperText={this.state.readonly ? undefined : (error != null ? error : this.translate(this.props.helperText))} autoComplete="off"
					   InputLabelProps={{ shrink: this.props.shrink !== null ? this.props.shrink : undefined }}
					   inputRef={this.inputRef}
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
					   }}/>
		);
	};

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
}

export default withStyles(styles, { withTheme: true })(withSnackbar(NumberEditable));
DisplayFactory.register("NumberEditable", withStyles(styles, { withTheme: true })(withSnackbar(NumberEditable)));