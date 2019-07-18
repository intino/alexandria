import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractTextExamplesMold from "../../../gen/displays/templates/AbstractTextExamplesMold";
import TextExamplesMoldNotifier from "../../../gen/displays/notifiers/TextExamplesMoldNotifier";
import TextExamplesMoldRequester from "../../../gen/displays/requesters/TextExamplesMoldRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class TextExamplesMold extends AbstractTextExamplesMold {

	constructor(props) {
		super(props);
		this.notifier = new TextExamplesMoldNotifier(this);
		this.requester = new TextExamplesMoldRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(TextExamplesMold);
DisplayFactory.register("TextExamplesMold", withStyles(styles, { withTheme: true })(TextExamplesMold));