import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractDateEditable from "../../../gen/displays/components/AbstractDateEditable";
import DateEditableNotifier from "../../../gen/displays/notifiers/DateEditableNotifier";
import DateEditableRequester from "../../../gen/displays/requesters/DateEditableRequester";
import MomentUtils from '@date-io/moment';
import { MuiPickersUtilsProvider, InlineDateTimePicker, InlineDatePicker } from 'material-ui-pickers';
import classnames from 'classnames';

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
		this.requester.notifyChange(moment.toDate().getTime());
		this.setState({ value: moment.toDate() });
	};

	render() {
		const { min, max, timePicker, classes } = this.props;
		const dateLabel = this.translate("date");
		const timeLabel = this.translate("time");
		const pattern = this.props.pattern !== "" ? this.props.pattern : undefined;
		const mask = this.props.mask != null ? this._parseMask() : undefined;
		return (
			<MuiPickersUtilsProvider utils={MomentUtils}>
				<div style={this.style()}>
					{ !timePicker ? <InlineDatePicker keyboard disableOpenOnEnter placeholder={pattern} mask={mask}
												format={pattern} className={classes.date}
												value={this.state.value} onChange={this.handleChange.bind(this)}
												min={min} max={max} label={dateLabel}/> : undefined }
					{ timePicker ? <InlineDateTimePicker keyboard disableOpenOnEnter placeholder={pattern} mask={mask}
												   format={pattern} className={classes.datetime}
												   value={this.state.value} onChange={this.handleChange.bind(this)}
												   min={min} max={max} label={timeLabel}/> : undefined }
				</div>
			</MuiPickersUtilsProvider>
		);
	};

	refresh = (value) => {
		this.setState({ value: new Date(value) });
	};

	_parseMask = () => {
		let result = "$" + this.props.mask.replace(/_/g, "##regex##").replace(/####/g, "##") + "$";
		result = result.replace("$##", "").replace("##$", "").replace(/$/, "").split("##");
		for (var i=0; i<result.length; i++)
			if (result[i] === "regex") result[i] = new RegExp("\\d");
		return result;
	}
}

export default withStyles(styles, { withTheme: true })(DateEditable);