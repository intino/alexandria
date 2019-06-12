import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractCollectionDialog from "../../../gen/displays/components/AbstractCollectionDialog";
import CollectionDialogNotifier from "../../../gen/displays/notifiers/CollectionDialogNotifier";
import CollectionDialogRequester from "../../../gen/displays/requesters/CollectionDialogRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';

const styles = theme => ({});

class CollectionDialog extends AbstractCollectionDialog {

	constructor(props) {
		super(props);
		this.notifier = new CollectionDialogNotifier(this);
		this.requester = new CollectionDialogRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(withSnackbar(CollectionDialog));
DisplayFactory.register("CollectionDialog", withStyles(styles, { withTheme: true })(withSnackbar(CollectionDialog)));