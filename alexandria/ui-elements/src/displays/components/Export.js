import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractExport from "../../../gen/displays/components/AbstractExport";
import ExportNotifier from "../../../gen/displays/notifiers/ExportNotifier";
import ExportRequester from "../../../gen/displays/requesters/ExportRequester";
import Operation from "./Operation";
import { Dialog, DialogTitle, DialogContent, DialogActions, Button } from "@material-ui/core";
import { KeyboardDatePicker, MuiPickersUtilsProvider } from '@material-ui/pickers';
import MomentUtils from "@date-io/moment";
import Select from '@material-ui/core/Select';
import DateUtil from "../../util/DateUtil";
import { withSnackbar } from 'notistack';

const styles = theme => ({
	...Operation.Styles
});

class Export extends AbstractExport {
	static DaySize = 1000*60*60*24;

	state = {
		openRange : false,
		from: this.props.from,
		to: this.props.to,
		option: null
	};

	constructor(props) {
		super(props);
		this.notifier = new ExportNotifier(this);
		this.requester = new ExportRequester(this);
	};

	render = () => {
		const operation = this.renderOperation();
		return (<React.Fragment>{this.renderRangeDialog()}{operation}</React.Fragment>);
	};

	renderRangeDialog = () => {
		const openRange = this.state.openRange != null ? this.state.openRange : false;
		return (<Dialog onClose={this.handleRangeClose} open={openRange}>
				<DialogTitle onClose={this.handleRangeClose}>{this._title()}</DialogTitle>
				<DialogContent style={{position:"relative",overflow:"hidden"}}>
					{this.renderOptions()}
					{this.renderRangePickers()}
				</DialogContent>
				<DialogActions>
					<Button onClick={this.handleRangeClose} color="primary">{this.translate("Cancel")}</Button>
					<Button variant="contained" onClick={this.handleRangeAccept} color="primary">{this.translate("OK")}</Button>
				</DialogActions>
			</Dialog>
		);
	};

	renderOptions = () => {
		if (this.props.options == null || this.props.options.length <= 1) return;
		return (<Select value={this.state.option}
						onChange={this.handleOptionChange}/>);
	};

	renderRangePickers = () => {
		const bounds = this._bounds();
		return (<React.Fragment>
					<MuiPickersUtilsProvider utils={MomentUtils}><KeyboardDatePicker variant="inline" autoOk format={this.translate("DD/MM/YYYY")}
										value={this.state.from} onChange={this.handleRangeFromChange.bind(this)}
										minDate={bounds.min} maxDate={bounds.max} label={this.translate("from")}/></MuiPickersUtilsProvider>
					&nbsp;&nbsp;
					<MuiPickersUtilsProvider utils={MomentUtils}><KeyboardDatePicker variant="inline" autoOk format={this.translate("DD/MM/YYYY")}
										value={this.state.to} onChange={this.handleRangeToChange.bind(this)}
										minDate={bounds.min} maxDate={bounds.max} label={this.translate("to")}/></MuiPickersUtilsProvider>
				</React.Fragment>
		);
	};

	handleRangeClose = () => {
		this.setState({ openRange: false });
	};

	handleRangeAccept = () => {
		if (!this.check(this._selection(), true)) return;
		this.execute();
		this.setState({ openRange: false });
	};

	handleRangeFromChange(moment) {
		if (!moment.isValid()) return false;
		const selection = { from: moment.toDate().getTime(), to: this.state.to, option: this.state.option };
		if (!this.check(selection)) return;
		this.requester.changeSelection(selection);
		this.setState({ from: moment.toDate().getTime() });
	};

	handleRangeToChange(moment) {
		if (!moment.isValid()) return;
		const selection = { from: this.state.from, to: moment.toDate().getTime(), option: this.state.option };
		if (!this.check(selection)) return;
		this.requester.changeSelection(selection);
		this.setState({ to: moment.toDate().getTime() });
	};

	handleOptionChange(e) {
		const option = e.target.value;
		this.requester.changeSelection({ from: this.state.from, to: this.state.to, option: option });
		this.setState({ option: option });
	};

	handleClick(e) {
		this.setState({ openRange: true });
	};

	check = (selection, checkRange) => {
		const from = selection.from;
		const to = selection.to;
		const min = this.state.min;
		const max = this.state.max;
		const range = this._range();
		const difference = Math.round((to - from)/Export.DaySize);
		if (from > this.state.to) { this.showError(this.translate("From date cannot be greater than to")); return false; }
		if (to < this.state.from) { this.showError(this.translate("To date cannot be lower than from")); return false; }
		if (min != null && from < min) { this.showError(this.translate("From date cannot be lower than " + DateUtil.format(min))); return false; }
		if (max != null && to > max) { this.showError(this.translate("To date cannot be greater than " + DateUtil.format(max))); return false; }
		if (range.min !== undefined && difference < range.min) { this.showMessage(this.translate("minimum allowed period is defined in {1} days").replace("{1}", range.min)); return !checkRange; }
		if (range.max !== undefined && difference > range.max) { this.showMessage(this.translate("maximum allowed period is defined in {1} days").replace("{1}", range.max)); return !checkRange; }
		return true;
	};

	showMessage : () => {
		voy por aqui... mostrar un mensaje constante para que lo vea el usuario
	};

	_bounds = () => {
		const min = this.props.min != null ? this.props.min : undefined;
		const max = this.props.max != null ? this.props.max : undefined;
		return { min: min, max: max };
	};

	_range = () => {
		return this.props.range != null ? this.props.range : {min:-1, max:-1};
	};

	_selection = () => {
		return { from: this.state.from, to: this.state.to, option: this.state.option };
	}
}

export default withStyles(styles, { withTheme: true })(withSnackbar(Export));