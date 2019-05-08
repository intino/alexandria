import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractDownloadSelectionExamplesMold from "../../../gen/displays/templates/AbstractDownloadSelectionExamplesMold";
import DownloadSelectionExamplesMoldNotifier from "../../../gen/displays/notifiers/DownloadSelectionExamplesMoldNotifier";
import DownloadSelectionExamplesMoldRequester from "../../../gen/displays/requesters/DownloadSelectionExamplesMoldRequester";

const styles = theme => ({});

class DownloadSelectionExamplesMold extends AbstractDownloadSelectionExamplesMold {

	constructor(props) {
		super(props);
		this.notifier = new DownloadSelectionExamplesMoldNotifier(this);
		this.requester = new DownloadSelectionExamplesMoldRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(DownloadSelectionExamplesMold);