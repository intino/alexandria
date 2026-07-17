import React from "react";
import {withStyles} from 'alexandria-ui-elements/src/util/muiStylesCompat';
import {Checkbox, FormControlLabel, IconButton} from '@mui/material';
import AbstractDateEditable from "../../../gen/displays/components/AbstractDateEditable";
import DateEditableNotifier from "../../../gen/displays/notifiers/DateEditableNotifier";
import DateEditableRequester from "../../../gen/displays/requesters/DateEditableRequester";
import {DatePicker, DateTimePicker, LocalizationProvider, TimePicker} from '@mui/x-date-pickers';
import {AdapterMoment} from '@mui/x-date-pickers/AdapterMoment';
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";
import 'alexandria-ui-elements/res/styles/layout.css';
import moment from 'moment';
import classNames from 'classnames';
import 'alexandria-ui-elements/res/styles/components/fields.css';
import Theme from "app-elements/gen/Theme";
import {errorFieldStyles, fieldErrorStyles, outlinedFieldStyles} from "./FieldStyles";

const styles = theme => ({
	date : outlinedFieldStyles(theme),
	datetime : outlinedFieldStyles(theme),
	errorField: errorFieldStyles(theme),
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
    error : fieldErrorStyles(theme, {
        padding: "0 18px",
    })
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

    handleError(e) {
        if (e == null || e === "") {
            this.lastError = null;
            return;
        }
        const element = document.getElementById(this.props.id + "-error");
        if (element == null) return;
        element.innerHTML = e;
        if (this.inputRef.current != null && this.inputRef.current !== document.activeElement) element.style.display = "block";
        this.lastError = e;
    };

    handleFocus() {
        const element = document.getElementById(this.props.id + "-error");
        if (element != null) element.style.display = "none";
    };

    handleBlur() {
        if (this.lastError == null || this.lastError === "") return;
        const element = document.getElementById(this.props.id + "-error");
        if (element != null) element.style.display = "block";
    };

	handleChange(moment) {
		if (moment != null && !moment.isValid()) return;
		this._notifyChange(moment != null ? this.noZoneDate(moment) : null);
	};

    render() {
        if (!this.state.visible) return (<React.Fragment/>);

        const { timePicker, classes } = this.props;
		const range = this.state.range;
		const min = range.min !== undefined && range.min != 0 ? range.min : this.props.mode === "fromnow" ? new Date() : undefined;
		const max = range.max !== undefined && range.max != 0 ? range.max : this.props.mode === "tonow" ? new Date() : undefined;
		const dateLabel = this.translate(this.props.label != null ? this.props.label : undefined);
		const timeLabel = this.translate(this.props.label != null ? this.props.label : undefined);
		const pattern = this.state.pattern;
		const value = this.state.value != null ? moment(this.state.value) : null;
		const toolbar = this._isEmbedded() && this.props.mode === "fromnow" ? (props) => (<React.Fragment/>) : undefined;
		const hasDateInfo = this._hasDateInfo();
		const hasTimeInfo = this._hasTimeInfo();
        const showTimePicker = timePicker || hasTimeInfo;
        const hasError = this.lastError != null && this.lastError !== "";
        const labelClasses = classNames(
            "MuiFormLabel-root",
            "MuiFormLabel-sizeSmall",
            "MuiFormLabel-filled",
            "MuiInputLabel-root",
            "MuiInputLabel-formControl",
            "MuiInputLabel-animated",
            "MuiInputLabel-shrink",
            "MuiInputLabel-sizeSmall",
            "MuiInputLabel-outlined",
            this.state.focused ? "Mui-focused" : undefined
        );
        const inputClasses = classNames(
            "MuiInputBase-root",
            "MuiInputBase-sizeSmall",
            "MuiOutlinedInput-root",
            "MuiOutlinedInput-sizeSmall",
            "MuiInputBase-formControl",
            "MuiInputBase-adornedEnd",
            this.state.focused ? "Mui-focused" : undefined,
            this.state.readonly ? "Mui-disabled" : undefined,
            "date-editable-input"
        );
        const textFieldSlotProps = {
            className: classNames(classes.date, hasError ? classes.errorField : undefined),
            inputRef: this.inputRef,
            disabled: this.state.readonly,
            onFocus: this.handleFocus.bind(this),
            onBlur: this.handleBlur.bind(this),
            slotProps: {
                inputLabel: {
                    shrink: hasError ? true : (this.state.readonly ? true : this.props.shrink !== null ? this.props.shrink : undefined),
                    className: labelClasses
                },
                input: {
                    className: inputClasses,
                    style: { borderRadius: "16px" }
                }
            },
            size: "small",
            variant: "outlined"
        };
		const theme = Theme.get();
		const isDark = theme != null && theme.palette != null && theme.palette.mode === "dark";
			return (
				<div style={{...this.style(),position:'relative'}} className={classNames("date-editable", this.state.readonly ? "readonly" : undefined, isDark ? "dark" : undefined)}>
                {!this.state.readonly && <div id={this.props.id + "-error"} className={classes.error} style={{display:'none'}}></div>}
				{ !showTimePicker ? <LocalizationProvider dateAdapter={AdapterMoment} adapterLocale={Application.configuration.language}>
                                    <DatePicker
                                            format={pattern} className={classes.date}
                                            value={value} onChange={this.handleChange.bind(this)}
                                            minDate={min != null ? moment(min) : undefined} maxDate={this._max(max) != null ? moment(this._max(max)) : undefined} label={dateLabel} views={this.views()}
                                            onError={this.handleError.bind(this)}
                                            slotProps={{ textField: textFieldSlotProps }}/>
                                </LocalizationProvider>
                             : undefined
                }
				{ (showTimePicker && hasDateInfo) ? <LocalizationProvider dateAdapter={AdapterMoment} adapterLocale={Application.configuration.language}>
				                    <DateTimePicker
                                            format={pattern} className={classes.datetime}
                                            value={value} onChange={this.handleChange.bind(this)}
                                            minDate={min != null ? moment(min) : undefined} maxDate={this._max(max) != null ? moment(this._max(max)) : undefined} label={timeLabel}
                                            onError={this.handleError.bind(this)}
                                            slotProps={{ textField: textFieldSlotProps }}/>
                                </LocalizationProvider>
                             : undefined
                }
				{ (showTimePicker && !hasDateInfo) ? <LocalizationProvider dateAdapter={AdapterMoment} adapterLocale={Application.configuration.language}>
				                    <TimePicker
				                            ampm={false}
                                            format={pattern} className={classes.datetime}
                                            value={value} onChange={this.handleChange.bind(this)}
                                            label={timeLabel}
                                            onError={this.handleError.bind(this)}
                                            slotProps={{ textField: textFieldSlotProps }}/>
                                </LocalizationProvider>
                             : undefined
                }
				{(!this.state.readonly && this.props.allowEmpty) && <FormControlLabel control={<Checkbox disabled={this.state.readonly} checked={this.state.empty} onChange={this.handleAllowEmpty.bind(this)} />} label={this.translate("sin definir")} />}
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
	    window.setTimeout(() => {
            if (this.state.readonly) this.inputRef.current.scrollIntoView();
            else this.inputRef.current.focus();
	    }, 100);
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
	    if (this.state.views == null) return undefined;
	    if (this.isWeekView()) return ["day"];
	    for (var i=0; i<this.state.views.length; i++) {
	        const view = this.state.views[i].toLowerCase();
	        if (view === "week" || view === "date") continue;
	        if (["year", "month", "day"].indexOf(view) === -1) continue;
	        result.push(view);
	    }
	    return result.length > 0 ? result : undefined;
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

	_hasDateInfo = () => {
        const pattern = this.state.pattern;
        if (pattern == null || pattern === "") return true;
        var infoList = ["M", "Mo", "MM", "MMM", "MMMM", "Q", "Qo", "D", "Do", "DD", "DDD", "DDDo", "DDDD", "d", "do", "dd", "ddd", "dddd", "e", "E", "w", "wo", "ww", "W", "Wo", "WW", "YY", "YYYY", "YYYYYY", "Y", "y", "N", "NN", "NNN", "NNNN", "NNNNN", "gg", "gggg", "GG", "GGGG"];
        for (var i=0; i<infoList.length; i++) if (pattern.indexOf(infoList[i]) != -1) return true;
        return false;
    };

    _hasTimeInfo = () => {
        const pattern = this.state.pattern;
        if (pattern == null || pattern === "") return false;
        const infoList = ["H", "HH", "h", "hh", "k", "kk", "m", "mm", "s", "ss", "A", "a"];
        for (var i=0; i<infoList.length; i++) if (pattern.indexOf(infoList[i]) != -1) return true;
        return false;
    };

}

export default withStyles(styles, { withTheme: true })(DateEditable);
DisplayFactory.register("DateEditable", withStyles(styles, { withTheme: true })(DateEditable));
