import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractSelectNextItem from "../../../gen/displays/components/AbstractSelectNextItem";
import SelectNextItemNotifier from "../../../gen/displays/notifiers/SelectNextItemNotifier";
import SelectNextItemRequester from "../../../gen/displays/requesters/SelectNextItemRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';

const styles = theme => ({});

class SelectNextItem extends AbstractSelectNextItem {

	constructor(props) {
		super(props);
		this.notifier = new SelectNextItemNotifier(this);
		this.requester = new SelectNextItemRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(withSnackbar(SelectNextItem));
DisplayFactory.register("SelectNextItem", withStyles(styles, { withTheme: true })(withSnackbar(SelectNextItem)));