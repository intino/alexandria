import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractTab from "../../../gen/displays/components/AbstractTab";
import TabNotifier from "../../../gen/displays/notifiers/TabNotifier";
import TabRequester from "../../../gen/displays/requesters/TabRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class Tab extends AbstractTab {

	constructor(props) {
		super(props);
		this.notifier = new TabNotifier(this);
		this.requester = new TabRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(Tab);
DisplayFactory.register("Tab", withStyles(styles, { withTheme: true })(Tab));