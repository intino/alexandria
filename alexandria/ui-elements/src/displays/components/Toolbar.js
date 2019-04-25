import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractToolbar from "../../../gen/displays/components/AbstractToolbar";
import ToolbarNotifier from "../../../gen/displays/notifiers/ToolbarNotifier";
import ToolbarRequester from "../../../gen/displays/requesters/ToolbarRequester";

const styles = theme => ({});

class Toolbar extends AbstractToolbar {

	constructor(props) {
		super(props);
		this.notifier = new ToolbarNotifier(this);
		this.requester = new ToolbarRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(Toolbar);