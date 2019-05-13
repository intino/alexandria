import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractDownloadExamplesMold from "../../../gen/displays/templates/AbstractDownloadExamplesMold";
import DownloadExamplesMoldNotifier from "../../../gen/displays/notifiers/DownloadExamplesMoldNotifier";
import DownloadExamplesMoldRequester from "../../../gen/displays/requesters/DownloadExamplesMoldRequester";

const styles = theme => ({});

class DownloadExamplesMold extends AbstractDownloadExamplesMold {

	constructor(props) {
		super(props);
		this.notifier = new DownloadExamplesMoldNotifier(this);
		this.requester = new DownloadExamplesMoldRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(DownloadExamplesMold);