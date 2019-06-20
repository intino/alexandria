import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractOpenPage from "../../../gen/displays/components/AbstractOpenPage";
import OpenPageNotifier from "../../../gen/displays/notifiers/OpenPageNotifier";
import OpenPageRequester from "../../../gen/displays/requesters/OpenPageRequester";
import Operation from "./Operation"
import { withSnackbar } from 'notistack';
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

class OpenPage extends AbstractOpenPage {
	constructor(props) {
		super(props);
		this.notifier = new OpenPageNotifier(this);
		this.requester = new OpenPageRequester(this);
		this.state = {
			...this.state,
			path : null
		};
	};

	open = (path) => {
		let url = this.buildApplicationUrl(path);
		if (this.props.format === "blank") window.open(url, "_blank");
		else window.location.href = url;
	};

}

export default withStyles(Operation.Styles, { withTheme: true })(withSnackbar(OpenPage));
DisplayFactory.register("OpenPage", withStyles(Operation.Styles, { withTheme: true })(withSnackbar(OpenPage)));