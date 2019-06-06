import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractDownloadSelection from "../../../gen/displays/components/AbstractDownloadSelection";
import DownloadSelectionNotifier from "../../../gen/displays/notifiers/DownloadSelectionNotifier";
import DownloadSelectionRequester from "../../../gen/displays/requesters/DownloadSelectionRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class DownloadSelection extends AbstractDownloadSelection {

	constructor(props) {
		super(props);
		this.notifier = new DownloadSelectionNotifier(this);
		this.requester = new DownloadSelectionRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(DownloadSelection);
DisplayFactory.register("DownloadSelection", withStyles(styles, { withTheme: true })(DownloadSelection));