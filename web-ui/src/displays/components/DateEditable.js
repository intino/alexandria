import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractDateEditable from "../../../gen/displays/components/AbstractDateEditable";
import DateEditableNotifier from "../../../gen/displays/notifiers/DateEditableNotifier";
import DateEditableRequester from "../../../gen/displays/requesters/DateEditableRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class DateEditable extends AbstractDateEditable {

	constructor(props) {
		super(props);
		this.notifier = new DateEditableNotifier(this);
		this.requester = new DateEditableRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(DateEditable);
DisplayFactory.register("DateEditable", withStyles(styles, { withTheme: true })(DateEditable));