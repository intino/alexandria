import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractDesktop from "../../gen/displays/AbstractDesktop";
import DesktopNotifier from "../../gen/displays/notifiers/DesktopNotifier";
import DesktopRequester from "../../gen/displays/requesters/DesktopRequester";

const styles = theme => ({});

class Desktop extends AbstractDesktop {

	constructor(props) {
		super(props);
		this.notifier = new DesktopNotifier(this);
		this.requester = new DesktopRequester(this);
	};

	render() {
        const { classes } = this.props;
		return (
			<React.Fragment></React.Fragment>
		);
	};


}

export default withStyles(styles, { withTheme: true })(Desktop);