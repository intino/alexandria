import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractCloseLayer from "../../../gen/displays/components/AbstractCloseLayer";
import CloseLayerNotifier from "../../../gen/displays/notifiers/CloseLayerNotifier";
import CloseLayerRequester from "../../../gen/displays/requesters/CloseLayerRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';
import { withSnackbar } from 'notistack';

const styles = theme => ({});

class CloseLayer extends AbstractCloseLayer {

	constructor(props) {
		super(props);
		this.notifier = new CloseLayerNotifier(this);
		this.requester = new CloseLayerRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(withSnackbar(CloseLayer));
DisplayFactory.register("CloseLayer", withStyles(styles, { withTheme: true })(withSnackbar(CloseLayer)));