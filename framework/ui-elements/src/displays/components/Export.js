import React, { Suspense } from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractExport from "../../../gen/displays/components/AbstractExport";
import ExportNotifier from "../../../gen/displays/notifiers/ExportNotifier";
import ExportRequester from "../../../gen/displays/requesters/ExportRequester";
import Operation from "./Operation";
import { Select, MenuItem, FormControl, FormHelperText, Dialog, DialogTitle, DialogContent, DialogActions, Button } from "@material-ui/core";
import { KeyboardDatePicker, MuiPickersUtilsProvider } from '@material-ui/pickers';
import MomentUtils from "@date-io/moment";
import DateUtil from "../../util/DateUtil";
import { withSnackbar } from 'notistack';
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

const styles = theme => ({
	...Operation.Styles,
	options : {
		marginBottom: "20px",
		width: "100%"
	},
	picker : {
		width: "calc(50% - 10px)"
	},
	pickerSeparator : {
		width: "20px",
		display: "inline-block"
	}
});

class Export extends AbstractExport {
	static DaySize = 1000*60*60*24;

	constructor(props) {
		super(props);
		this.notifier = new ExportNotifier(this);
		this.requester = new ExportRequester(this);
		this.state = {
			...this.state,
			openRange : false,
			from: this.props.from,
			to: this.props.to,
			option: this.props.options != null && this.props.options.length > 0 ? this.props.options[0] : "",
			exportDisabled : false
		};
	};

	render = () => {
		const operation = this.renderOperation();
		return (
			<Suspense fallback={<div style={{width: "24px", ...this.style()}}/>}>
				{this.renderRangeDialog()}{operation}
			</Suspense>
		);
	};

	renderRangeDialog = () => {
		const openRange = this.state.openRange != null ? this.state.openRange : false;
		return (<Dialog onClose={this.handleExportClose} open={openRange}>
				<DialogTitle onClose={this.handleExportClose}>{this._title()}</DialogTitle>
				<DialogContent style={{position:"relative",overflow:"hidden"}}>
					{this.renderOptions()}
					{this.renderRangePickers()}
				</DialogContent>
				<DialogActions>
					<Button onClick={this.handleExportClose} color="primary">{this.translate("Cancel")}</Button>
					<Button variant="contained" onClick={this.handleExportAccept} color="primary" disabled={this.state.exportDisabled}>{this.translate("OK")}</Button>
				</DialogActions>
			</Dialog>
		);
	};

	renderOptions = () => {
		const options = this._options();
		if (options.length <= 1) return;
		return (<FormControl className={this.props.classes.options}>
					<Select value={this.state.option} style={{width:"100%"}} displayEmpty onChange={this.handleOptionChange.bind(this)}>
						<MenuItem value="" disabled>{this.translate("Select an option")}</MenuItem>
						{options.map((option, i) => <MenuItem key={i} value={option}>{option}</MenuItem>)}
					</Select>
				</FormControl>
		);
	};

	renderRangePickers = () => {
		const bounds = this._bounds();
		const { classes } = this.props;
		return (<React.Fragment>
					<MuiPickersUtilsProvider utils={MomentUtils}><KeyboardDatePicker variant="inline" autoOk format={this._format()}
									 	className={classes.picker}
										value={this.state.from} onChange={this.handleFromChange.bind(this)}
										minDate={bounds.min} maxDate={bounds.max} label={this.translate("from")}
										minDateMessage={this.translate("Date should not be before minimal date")}
										maxDateMessage={this.translate("Date should not be after maximal date")}/></MuiPickersUtilsProvider>
					<span className={classes.pickerSeparator}></span>
					<MuiPickersUtilsProvider utils={MomentUtils}><KeyboardDatePicker variant="inline" autoOk format={this._format()}
									    className={classes.picker}
										value={this.state.to} onChange={this.handleToChange.bind(this)}
										minDate={bounds.min} maxDate={bounds.max} label={this.translate("to")}
										minDateMessage={this.translate("Date should not be before minimal date")}
										maxDateMessage={this.translate("Date should not be after maximal date")}/></MuiPickersUtilsProvider>
					{this.renderMessages()}
				</React.Fragment>
		);
	};

	renderMessages = () => {
		return (<FormHelperText>{this.renderBoundsMessage()}<br/>{this.renderRangeMessage()}</FormHelperText>);
	};

	renderBoundsMessage = () => {
		const bounds = this._bounds();
		let message = null;
		if (bounds.min !== -1 && bounds.max !== -1) message = this.translate("Select dates in range {1} and {2}, inclusive", [ DateUtil.format(bounds.min, this._format()), DateUtil.format(bounds.max, this._format()) ]);
		else if (bounds.min !== -1) message = this.translate("Select dates after {1}", [ DateUtil.format(bounds.min, this._format()) ]);
		else if (bounds.max !== -1) message = this.translate("Select dates before {1}", [ DateUtil.format(bounds.max, this._format()) ]);
		return message;
	};

	renderRangeMessage = () => {
		const range = this._range();
		let message = null;
		if (range.min !== -1 && range.max !== -1) message = this.translate("Select at least {1} days with a maximum of {2}", [ range.min, range.max ]);
		else if (range.min !== -1) message = this.translate("Select at least {1} days", [ range.min ]);
		else if (range.max !== -1) message = this.translate("Select a maximum of {1} days", [ range.max ]);
		return message;
	};

	handleExportClose = () => {
		this.setState({ openRange: false });
	};

	handleExportAccept = () => {
		if (!this.valid(this._params())) return;
		this.execute();
		this.setState({ openRange: false });
	};

	handleFromChange(moment) {
		if (!moment.isValid()) return false;
		const params = { from: moment.toDate().getTime(), to: this.attribute("to"), option: this.attribute("option") };
		this.setState({ from: moment.toDate().getTime(), exportDisabled: !this.valid(params) });
		if (this.valid(params)) this.requester.changeParams(params);
	};

	handleToChange(moment) {
		if (!moment.isValid()) return;
		const params = { from: this.attribute("from"), to: moment.toDate().getTime(), option: this.attribute("option") };
		this.setState({ to: moment.toDate().getTime(), exportDisabled: !this.valid(params) });
		if (this.valid(params)) this.requester.changeParams(params);
	};

	handleOptionChange(e) {
		const option = e.target.value;
		this.requester.changeParams({ from: this.attribute("from"), to: this.attribute("to"), option: option });
		this.setState({ option: option });
	};

	handleClick(e) {
		const options = this._options();
		const option = options.length > 0 ? options[0] : "";
		const from = this._bounds().min !== undefined ? this._bounds().min : new Date().getTime();
		const to = from + (this._range().max !== -1 ? this._range().max*Export.DaySize : (this._range().min !== -1 ? this._range().min*Export.DaySize : 0));
		const params = { from: from, to: to, option: option };
		this.requester.changeParams(params);
		this.setState({ ...params, openRange: true });
	};

	valid = (params) => {
		if (!this.checkBounds(params)) return false;
		if (this._options().length > 0 && params.option == null) return false;
		return this.checkRange(params);
	};

	checkBounds = (params) => {
		const from = params.from;
		const to = params.to;
		const bounds = this._bounds();
		if (bounds.min != null && (from < bounds.min || to < bounds.min)) { return false; }
		if (bounds.max != null && (from > bounds.max || to > bounds.max)) { return false; }
		if (from > to) { this.showError(this.translate("From date cannot be greater than to")); return false; }
		if (to < from) { this.showError(this.translate("To date cannot be lower than from")); return false; }
		return true;
	};

	checkRange = (params) => {
		const range = this._range();
		const difference = Math.round((params.to - params.from)/Export.DaySize);
		if (range.min !== undefined && range.min !== -1 && difference < range.min) { this.showError(this.translate("minimum allowed period is defined in {1} days", [ range.min ])); return false; }
		if (range.max !== undefined && range.max !== -1 && difference > range.max) { this.showError(this.translate("maximum allowed period is defined in {1} days", [ range.max ])); return false; }
		return true;
	};

	_bounds = () => {
		const min = this.props.min != null ? this.props.min : undefined;
		const max = this.props.max != null ? this.props.max : undefined;
		return { min: min, max: max };
	};

	_range = () => {
		return this.props.range != null ? this.props.range : {min:-1, max:-1};
	};

	_params = () => {
		return { from: this.attribute("from"), to: this.attribute("to"), option: this.attribute("option") };
	};

	_options = () => {
		return this.props.options != null ? this.props.options : [];
	};

	_format = () => {
		return this.translate("DD/MM/YYYY");
	}
}

export default withStyles(styles, { withTheme: true })(withSnackbar(Export));
DisplayFactory.register("Export", withStyles(styles, { withTheme: true })(withSnackbar(Export)));