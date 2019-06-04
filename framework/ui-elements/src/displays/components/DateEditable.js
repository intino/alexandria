import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractDateEditable from "../../../gen/displays/components/AbstractDateEditable";
import DateEditableNotifier from "../../../gen/displays/notifiers/DateEditableNotifier";
import DateEditableRequester from "../../../gen/displays/requesters/DateEditableRequester";
import MomentUtils from '@date-io/moment';
import { MuiPickersUtilsProvider, KeyboardDateTimePicker, KeyboardDatePicker } from '@material-ui/pickers';
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

const styles = props => ({
	date : {
		width: "100%"
	},
	datetime : {
		width: "100%"
	}
});

class DateEditable extends AbstractDateEditable {
	state = {
		value : this.props.value
	};

	constructor(props) {
		super(props);
		this.notifier = new DateEditableNotifier(this);
		this.requester = new DateEditableRequester(this);
	};

	handleChange(moment) {
		if (!moment.isValid()) return;
		this.requester.notifyChange(moment.toDate().getTime());
		this.setState({ value: moment.toDate() });
	};

	render() {
		const { min, max, timePicker, classes } = this.props;
		const dateLabel = this.translate("date");
		const timeLabel = this.translate("time");
		const pattern = this.props.pattern !== "" ? this.props.pattern : undefined;
		return (<div style={this.style()}>
					{ !timePicker ? <MuiPickersUtilsProvider utils={MomentUtils}><KeyboardDatePicker variant="inline" placeholder={pattern} autoOk
												format={pattern} className={classes.date} mask={this.props.mask}
												value={this.state.value} onChange={this.handleChange.bind(this)}
                                                minDate={min} maxDate={max} label={dateLabel}
												minDateMessage={this.translate("Date should not be before minimal date")}
												maxDateMessage={this.translate("Date should not be after maximal date")}/></MuiPickersUtilsProvider> : undefined }
					{ timePicker ? <MuiPickersUtilsProvider utils={MomentUtils}><KeyboardDateTimePicker variant="inline" placeholder={pattern} autoOk
												   format={pattern} className={classes.datetime}
												   value={this.state.value} onChange={this.handleChange.bind(this)}
                                                   minDate={min} maxDate={max} label={timeLabel}
												   minDateMessage={this.translate("Date should not be before minimal date")}
												   maxDateMessage={this.translate("Date should not be after maximal date")}/></MuiPickersUtilsProvider> : undefined }
				</div>
		);
	};

	refresh = (value) => {
		this.setState({ value: new Date(value) });
	};

}

export default withStyles(styles, { withTheme: true })(DateEditable);
DisplayFactory.register("DateEditable", withStyles(styles, { withTheme: true })(DateEditable));