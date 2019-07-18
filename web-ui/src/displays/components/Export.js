import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractExport from "../../../gen/displays/components/AbstractExport";
import ExportNotifier from "../../../gen/displays/notifiers/ExportNotifier";
import ExportRequester from "../../../gen/displays/requesters/ExportRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class Export extends AbstractExport {

	constructor(props) {
		super(props);
		this.notifier = new ExportNotifier(this);
		this.requester = new ExportRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(Export);
DisplayFactory.register("Export", withStyles(styles, { withTheme: true })(Export));