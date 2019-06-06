import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractText from "../../../gen/displays/components/AbstractText";
import TextNotifier from "../../../gen/displays/notifiers/TextNotifier";
import TextRequester from "../../../gen/displays/requesters/TextRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class Text extends AbstractText {

	constructor(props) {
		super(props);
		this.notifier = new TextNotifier(this);
		this.requester = new TextRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(Text);
DisplayFactory.register("Text", withStyles(styles, { withTheme: true })(Text));