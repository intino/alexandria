import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractOpenPreviousItem from "../../../gen/displays/components/AbstractOpenPreviousItem";
import OpenPreviousItemNotifier from "../../../gen/displays/notifiers/OpenPreviousItemNotifier";
import OpenPreviousItemRequester from "../../../gen/displays/requesters/OpenPreviousItemRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';

const styles = theme => ({});

class OpenPreviousItem extends AbstractOpenPreviousItem {

	constructor(props) {
		super(props);
		this.notifier = new OpenPreviousItemNotifier(this);
		this.requester = new OpenPreviousItemRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(withSnackbar(OpenPreviousItem));
DisplayFactory.register("OpenPreviousItem", withStyles(styles, { withTheme: true })(withSnackbar(OpenPreviousItem)));