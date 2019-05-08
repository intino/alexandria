import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractGroupBox from "../../../gen/displays/components/AbstractGroupBox";
import GroupBoxNotifier from "../../../gen/displays/notifiers/GroupBoxNotifier";
import GroupBoxRequester from "../../../gen/displays/requesters/GroupBoxRequester";

const styles = theme => ({});

class GroupBox extends AbstractGroupBox {

	constructor(props) {
		super(props);
		this.notifier = new GroupBoxNotifier(this);
		this.requester = new GroupBoxRequester(this);
	};

	render() {
		return (
			<React.Fragment>
				hola mundo!!!
			</React.Fragment>
		);
	}

}

export default withStyles(styles, { withTheme: true })(GroupBox);