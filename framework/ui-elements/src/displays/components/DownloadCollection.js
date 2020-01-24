import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractDownloadCollection from "../../../gen/displays/components/AbstractDownloadCollection";
import DownloadCollectionNotifier from "../../../gen/displays/notifiers/DownloadCollectionNotifier";
import DownloadCollectionRequester from "../../../gen/displays/requesters/DownloadCollectionRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';

const styles = theme => ({});

class DownloadCollection extends AbstractDownloadCollection {

	constructor(props) {
		super(props);
		this.notifier = new DownloadCollectionNotifier(this);
		this.requester = new DownloadCollectionRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(withSnackbar(DownloadCollection));
DisplayFactory.register("DownloadCollection", withStyles(styles, { withTheme: true })(withSnackbar(DownloadCollection)));