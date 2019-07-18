import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractBaseText from "../../../gen/displays/components/AbstractBaseText";
import BaseTextNotifier from "../../../gen/displays/notifiers/BaseTextNotifier";
import BaseTextRequester from "../../../gen/displays/requesters/BaseTextRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class BaseText extends AbstractBaseText {

	constructor(props) {
		super(props);
		this.notifier = new BaseTextNotifier(this);
		this.requester = new BaseTextRequester(this);
	};

	refresh = (value) => {
	};
}

export default withStyles(styles, { withTheme: true })(BaseText);
DisplayFactory.register("BaseText", withStyles(styles, { withTheme: true })(BaseText));