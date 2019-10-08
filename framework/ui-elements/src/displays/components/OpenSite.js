import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractOpenSite from "../../../gen/displays/components/AbstractOpenSite";
import OpenSiteNotifier from "../../../gen/displays/notifiers/OpenSiteNotifier";
import OpenSiteRequester from "../../../gen/displays/requesters/OpenSiteRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';
import Operation from "./Operation"

class OpenSite extends AbstractOpenSite {

	constructor(props) {
		super(props);
		this.notifier = new OpenSiteNotifier(this);
		this.requester = new OpenSiteRequester(this);
		this.state = {
			...this.state,
			path : null
		};
	};

	open = (site) => {
		if (this.props.target === "blank") window.open(site, "_blank");
		else window.location.href = site;
	};
}

export default withStyles(Operation.Styles, { withTheme: true })(withSnackbar(OpenSite));
DisplayFactory.register("OpenSite", withStyles(Operation.Styles, { withTheme: true })(withSnackbar(OpenSite)));