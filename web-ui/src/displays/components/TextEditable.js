import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractTextEditable from "../../../gen/displays/components/AbstractTextEditable";
import TextEditableNotifier from "../../../gen/displays/notifiers/TextEditableNotifier";
import TextEditableRequester from "../../../gen/displays/requesters/TextEditableRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class TextEditable extends AbstractTextEditable {

	constructor(props) {
		super(props);
		this.notifier = new TextEditableNotifier(this);
		this.requester = new TextEditableRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(TextEditable);
DisplayFactory.register("TextEditable", withStyles(styles, { withTheme: true })(TextEditable));