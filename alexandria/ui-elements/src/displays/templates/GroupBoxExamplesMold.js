import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractGroupBoxExamplesMold from "../../../gen/displays/templates/AbstractGroupBoxExamplesMold";
import GroupBoxExamplesMoldNotifier from "../../../gen/displays/notifiers/GroupBoxExamplesMoldNotifier";
import GroupBoxExamplesMoldRequester from "../../../gen/displays/requesters/GroupBoxExamplesMoldRequester";

const styles = theme => ({});

class GroupBoxExamplesMold extends AbstractGroupBoxExamplesMold {

	constructor(props) {
		super(props);
		this.notifier = new GroupBoxExamplesMoldNotifier(this);
		this.requester = new GroupBoxExamplesMoldRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(GroupBoxExamplesMold);