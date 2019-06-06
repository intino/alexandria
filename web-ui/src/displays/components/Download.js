import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractDownload from "../../../gen/displays/components/AbstractDownload";
import DownloadNotifier from "../../../gen/displays/notifiers/DownloadNotifier";
import DownloadRequester from "../../../gen/displays/requesters/DownloadRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class Download extends AbstractDownload {

	constructor(props) {
		super(props);
		this.notifier = new DownloadNotifier(this);
		this.requester = new DownloadRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(Download);
DisplayFactory.register("Download", withStyles(styles, { withTheme: true })(Download));