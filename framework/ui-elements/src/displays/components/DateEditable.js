import React from "react";
import { withStyles } from '@material-ui/core/styles';
import { FormControlLabel, Checkbox, IconButton } from '@material-ui/core';
import AbstractDateEditable from "../../../gen/displays/components/AbstractDateEditable";
import DateEditableNotifier from "../../../gen/displays/notifiers/DateEditableNotifier";
import DateEditableRequester from "../../../gen/displays/requesters/DateEditableRequester";
import MomentUtils from '@date-io/moment';
import { MuiPickersUtilsProvider, KeyboardDateTimePicker, KeyboardDatePicker } from '@material-ui/pickers';
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";
import 'alexandria-ui-elements/res/styles/layout.css';
import moment from 'moment';
import classNames from 'classnames';

const styles = theme => ({
	date : {
		width: "100%"
	},
	datetime : {
		width: "100%"
	},
	dayWrapper: {
        position: "relative",
    },
    day: {
        width: 36,
        height: 36,
        fontSize: theme.typography.caption.fontSize,
        margin: "0 2px",
        color: "inherit",
    },
    customDayHighlight: {
        position: "absolute",
        top: 0,
        bottom: 0,
        left: "2px",
        right: "2px",
        border: `1px solid ${theme.palette.secondary.main}`,
        borderRadius: "50%",
    },
    nonCurrentMonthDay: {
        color: theme.palette.text.disabled,
    },
    highlightNonCurrentMonthDay: {
        color: "#676767",
    },
    highlight: {
        background: theme.palette.primary.main,
        color: theme.palette.common.white,
    },
    firstHighlight: {
        background: theme.palette.primary.main,
        color: theme.palette.common.white,
        borderTopLeftRadius: "50%",
        borderBottomLeftRadius: "50%",
    },
    endHighlight: {
        background: theme.palette.primary.main,
        color: theme.palette.common.white,
        borderTopRightRadius: "50%",
        borderBottomRightRadius: "50%",
    },
});

class DateEditable extends AbstractDateEditable {

	constructor(props) {
		super(props);
		this.notifier = new DateEditableNotifier(this);
		this.requester = new DateEditableRequester(this);
		this.inputRef = React.createRef();
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
		const min = range.min !== undefined && range.min != 0 ? range.min : undefined;
		const max = range.max !== undefined && range.max != 0 ? range.max : undefined;
		const dateLabel = this.translate(this.props.label != null ? this.props.label : undefined);
		const timeLabel = this.translate(this.props.label != null ? this.props.label : undefined);
		const pattern = this.state.pattern;
		const value = this.state.value != null ? this.state.value : null;
		const variant = this._variant();
		const toolbar = this._isEmbedded() && this.props.mode === "fromnow" ? (props) => (<React.Fragment/>) : undefined;
		return (
			<div style={this.style()}>
				{ !timePicker ? <MuiPickersUtilsProvider libInstance={moment} utils={MomentUtils} locale={moment.locale(Application.configuration.language)}>
                                    <KeyboardDatePicker variant={variant} placeholder={pattern} autoOk
                                            inputProps={{ref:this.inputRef}}
                                            disabled={this.state.readonly}
                                            format={pattern} className={classes.date} mask={this.props.mask}
                                            value={value} onChange={this.handleChange.bind(this)}
                                            minDate={min} maxDate={this._max(max)} label={dateLabel} views={this.views()}
                                            ToolbarComponent={toolbar}
                                            renderDay={this.isWeekView() ? this.renderWrappedWeekDay.bind(this) : undefined}
                                            minDateMessage={this.translate("Date should not be before minimal date")}
                                            maxDateMessage={this.translate("Date should not be after maximal date")}/>
                                </MuiPickersUtilsProvider>
                             : undefined
                }
				{ timePicker ? <MuiPickersUtilsProvider libInstance={moment} utils={MomentUtils} locale={moment.locale(Application.configuration.language)}>
				                    <KeyboardDateTimePicker variant={variant} placeholder={pattern} autoOk
                                            inputProps={{ref:this.inputRef}}
                                            disabled={this.state.readonly}
                                            format={pattern} className={classes.datetime}
                                            value={value} onChange={this.handleChange.bind(this)}
                                            minDate={min} maxDate={this._max(max)} label={timeLabel}
                                            ToolbarComponent={toolbar}
                                            renderDay={this.isWeekView() ? this.renderWrappedWeekDay.bind(this) : undefined}
                                            minDateMessage={this.translate("Date should not be before minimal date")}
                                            maxDateMessage={this.translate("Date should not be after maximal date")}/>
                                </MuiPickersUtilsProvider>
                             : undefined
                }
				{this.props.allowEmpty && <FormControlLabel control={<Checkbox disabled={this.state.readonly} checked={this.state.empty} onChange={this.handleAllowEmpty.bind(this)} />} label={this.translate("sin definir")} />}
			</div>
		);
	};

	_variant = () => {
	    return this._isEmbedded() ? "static" : "inline";
	};

	_isEmbedded = () => {
	    const embedded = this.props.embedded;
	    return embedded != null && embedded;
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

	refreshFocused = (focused) => {
        if (this.inputRef == null || this.inputRef.current == null) return;
        window.setTimeout(() => this.inputRef.current.focus(), 100);
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
	    const newValue = this._normalize(date);
		this.requester.notifyChange(newValue != null ? newValue.getTime() : null);
		this.setState({ value: newValue != null ? newValue : null, empty: newValue === null || newValue === ""});
	};

	_normalize = (date) => {
	    if (date == null) return date;
	    const weekView = this.isWeekView();
	    if (!weekView) return date;
	    return moment(date).startOf('week').toDate();
	};

	views = () => {
	    const result = [];
	    if (this.state.views == null || this.isWeekView()) return ["date"];
	    for (var i=0; i<this.state.views.length; i++) {
	        if (this.state.views[i].toLowerCase() === "week") continue;
	        result.push(this.state.views[i].toLowerCase());
	    }
	    return result;
	};

	isWeekView = () => {
	    const result = [];
	    if (this.state.views == null) return false;
	    for (var i=0; i<this.state.views.length; i++) {
	        if (this.state.views[i].toLowerCase() === "week") return true;
	    }
	    return false;
	};

    noZoneDate = (localeMoment) => {
        var m1 = moment(localeMoment);
        var offsetInMinutes = m1.utcOffset();
        return m1.utc().add(offsetInMinutes, 'm').toDate();
    };

    renderWrappedWeekDay = (date, selectedDate, dayInCurrentMonth) => {
		const range = this.state.range;
		const min = range.min !== undefined && range.min != 0 ? moment.utc(range.min) : undefined;
		const max = range.max !== undefined && range.max != 0 ? moment.utc(range.max) : undefined;
        const { classes } = this.props;
        let dateClone = moment.utc(date);
        const now = moment(new Date());
        const start = moment.utc(selectedDate).startOf('week');
        const end = moment.utc(selectedDate).endOf('week');
        const maxEnd = max !== undefined ? moment.utc(max).endOf('week') : null;

        const isFirstDay = dateClone.isSame(start, 'day');
        const isLastDay = dateClone.isSame(end, 'day');
        const dayIsBetween = dateClone.isBetween(start, end, 'days') || isLastDay;
        const readonly = (min !== undefined && date.isBefore(min, 'day')) || (max !== undefined && date.isAfter(maxEnd, 'day'));

        const wrapperClassName = classNames({
          [classes.highlight]: dayIsBetween,
          [classes.firstHighlight]: isFirstDay,
          [classes.endHighlight]: isLastDay,
          [classes.readonly]: readonly && !dayIsBetween,
        });

        const dayClassName = classNames(classes.day, {
          [classes.nonCurrentMonthDay]: !dayInCurrentMonth,
          [classes.highlightNonCurrentMonthDay]: !dayInCurrentMonth && dayIsBetween,
        });

        return (
          <div className={wrapperClassName}>
            <IconButton className={dayClassName} disabled={readonly && !dayIsBetween}>
              <span>{moment.utc(dateClone).date()}</span>
            </IconButton>
          </div>
        );
    };

    _max = (value) => {
        if (!this.isWeekView()) return value;
        if (value == null) return undefined;
        if (value == undefined) return undefined;
        return moment.utc(value).endOf('week').toDate();
    };
}

export default withStyles(styles, { withTheme: true })(DateEditable);
DisplayFactory.register("DateEditable", withStyles(styles, { withTheme: true })(DateEditable));