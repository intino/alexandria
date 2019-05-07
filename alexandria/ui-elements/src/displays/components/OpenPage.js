import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractOpenPage from "../../../gen/displays/components/AbstractOpenPage";
import OpenPageNotifier from "../../../gen/displays/notifiers/OpenPageNotifier";
import OpenPageRequester from "../../../gen/displays/requesters/OpenPageRequester";
import Operation from "./Operation"

class OpenPage extends AbstractOpenPage {
	state = {
		path : null
	};

	constructor(props) {
		super(props);
		this.notifier = new OpenPageNotifier(this);
		this.requester = new OpenPageRequester(this);
	};

	redirect = (path) => {
		let url = this.buildApplicationUrl(path);
		if (this.props.format === "blank") window.open(url, "_blank");
		else window.location.href = url;
	};

}

export default withStyles(Operation.Styles, { withTheme: true })(OpenPage);