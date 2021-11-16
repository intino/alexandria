import React from "react";
import { withStyles } from '@material-ui/core/styles';
import { FormControlLabel, Checkbox } from '@material-ui/core';
import AbstractDateEditable from "../../../gen/displays/components/AbstractDateEditable";
import DateEditableNotifier from "../../../gen/displays/notifiers/DateEditableNotifier";
import DateEditableRequester from "../../../gen/displays/requesters/DateEditableRequester";
import MomentUtils from '@date-io/moment';
import { MuiPickersUtilsProvider, KeyboardDateTimePicker, KeyboardDatePicker } from '@material-ui/pickers';
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";
import 'alexandria-ui-elements/res/styles/layout.css';
import moment from 'moment';

const styles = props => ({
	date : {
		width: "100%"
	},
	datetime : {
		width: "100%"
	}
});

class DateEditable extends AbstractDateEditable {

	constructor(props) {
		super(props);
		this.notifier = new DateEditableNotifier(this);
		this.requester = new DateEditableRequester(this);
        this.state = {
            ...this.state,
            pattern : this.props.pattern !== "" ? this.props.pattern : undefined,
            range : { min: this.props.min, max: this.props.max },
            value : this.props.value,
            readonly : this.props.readonly,
            views : this.props.views,
            empty : this.props.value == null,
        };
	};

	handleChange(moment) {
		if (moment != null && !moment.isValid()) return;
		this._notifyChange(moment != null ? this.noZoneDate(moment) : null);
	};

	render() {
		if (!this.state.visible) return (<React.Fragment/>);

		const { timePicker, classes } = this.props;
		const range = this.state.range;
		const dateLabel = this.translate(this.props.label != null ? this.props.label : undefined);
		const timeLabel = this.translate(this.props.label != null ? this.props.label : undefined);
		const pattern = this.state.pattern;
		const value = this.state.value != null ? this.state.value : null;
		return (
			<div style={this.style()}>
				{ !timePicker ? <MuiPickersUtilsProvider utils={MomentUtils}><KeyboardDatePicker variant="inline" placeholder={pattern} autoOk
																								 disabled={this.state.readonly}
																								 format={pattern} className={classes.date} mask={this.props.mask}
																								 value={value} onChange={this.handleChange.bind(this)}
																								 minDate={range.min} maxDate={range.max} label={dateLabel} views={this.views()}
																								 minDateMessage={this.translate("Date should not be before minimal date")}
																								 maxDateMessage={this.translate("Date should not be after maximal date")}/></MuiPickersUtilsProvider> : undefined }
				{ timePicker ? <MuiPickersUtilsProvider utils={MomentUtils}><KeyboardDateTimePicker variant="inline" placeholder={pattern} autoOk
																									disabled={this.state.readonly}
																									format={pattern} className={classes.datetime}
																									value={value} onChange={this.handleChange.bind(this)}
																									minDate={range.min} maxDate={range.max} label={timeLabel}
																									minDateMessage={this.translate("Date should not be before minimal date")}
																									maxDateMessage={this.translate("Date should not be after maximal date")}/></MuiPickersUtilsProvider> : undefined }
				{this.props.allowEmpty && <FormControlLabel control={<Checkbox disabled={this.state.readonly} checked={this.state.empty} onChange={this.handleAllowEmpty.bind(this)} />} label={this.translate("sin definir")} />}
			</div>
		);
	};

	refresh = (value) => {
	    const _date = new Date(value);
		const _utcDate = new Date(_date.getUTCFullYear(), _date.getUTCMonth(), _date.getUTCDate(), _date.getUTCHours(), _date.getUTCMinutes(), _date.getUTCSeconds(), _date.getUTCMilliseconds())
		const date = value != null ? _utcDate : null;
        this.setState({ value: date, empty: date === null });
	};

	refreshPattern = (pattern) => {
	    this.setState({ pattern });
	};

	refreshReadonly = (readonly) => {
		this.setState({ readonly });
	};

	refreshRange = (range) => {
		this.setState({ range });
	};

	refreshViews = (views) => {
		this.setState({ views });
	};

	handleAllowEmpty = (e) => {
		const checked = e.target.checked;
		this._notifyChange(checked ? null : new Date());
	};

	_notifyChange = (date) => {
		this.requester.notifyChange(date != null ? date.getTime() : null);
		this.setState({ value: date != null ? date : null, empty: date === null || date === ""});
	};

	views = () => {
	    const result = [];
	    if (this.state.views == null) return ["date"];
	    for (var i=0; i<this.state.views.length; i++) result.push(this.state.views[i].toLowerCase());
	    return result;
	};

    noZoneDate = (localeMoment) => {
        var m1 = moment(localeMoment);
        var offsetInMinutes = m1.utcOffset();
        return m1.utc().add(offsetInMinutes, 'm').toDate();
    };

}

export default withStyles(styles, { withTheme: true })(DateEditable);
DisplayFactory.register("DateEditable", withStyles(styles, { withTheme: true })(DateEditable));