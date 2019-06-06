import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractNumberEditable from "../../../gen/displays/components/AbstractNumberEditable";
import NumberEditableNotifier from "../../../gen/displays/notifiers/NumberEditableNotifier";
import NumberEditableRequester from "../../../gen/displays/requesters/NumberEditableRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class NumberEditable extends AbstractNumberEditable {

	constructor(props) {
		super(props);
		this.notifier = new NumberEditableNotifier(this);
		this.requester = new NumberEditableRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(NumberEditable);
DisplayFactory.register("NumberEditable", withStyles(styles, { withTheme: true })(NumberEditable));