import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractBrochure from "../../../gen/displays/components/AbstractBrochure";
import BrochureNotifier from "../../../gen/displays/notifiers/BrochureNotifier";
import BrochureRequester from "../../../gen/displays/requesters/BrochureRequester";

const styles = theme => ({});

class Brochure extends AbstractBrochure {

	constructor(props) {
		super(props);
		this.notifier = new BrochureNotifier(this);
		this.requester = new BrochureRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(Brochure);