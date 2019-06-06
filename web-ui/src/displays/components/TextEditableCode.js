import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractTextEditableCode from "../../../gen/displays/components/AbstractTextEditableCode";
import TextEditableCodeNotifier from "../../../gen/displays/notifiers/TextEditableCodeNotifier";
import TextEditableCodeRequester from "../../../gen/displays/requesters/TextEditableCodeRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class TextEditableCode extends AbstractTextEditableCode {

	constructor(props) {
		super(props);
		this.notifier = new TextEditableCodeNotifier(this);
		this.requester = new TextEditableCodeRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(TextEditableCode);
DisplayFactory.register("TextEditableCode", withStyles(styles, { withTheme: true })(TextEditableCode));