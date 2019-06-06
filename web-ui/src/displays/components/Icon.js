import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractIcon from "../../../gen/displays/components/AbstractIcon";
import IconNotifier from "../../../gen/displays/notifiers/IconNotifier";
import IconRequester from "../../../gen/displays/requesters/IconRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class Icon extends AbstractIcon {

	constructor(props) {
		super(props);
		this.notifier = new IconNotifier(this);
		this.requester = new IconRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(Icon);
DisplayFactory.register("Icon", withStyles(styles, { withTheme: true })(Icon));