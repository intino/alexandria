import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractExportExamplesMold from "../../../gen/displays/templates/AbstractExportExamplesMold";
import ExportExamplesMoldNotifier from "../../../gen/displays/notifiers/ExportExamplesMoldNotifier";
import ExportExamplesMoldRequester from "../../../gen/displays/requesters/ExportExamplesMoldRequester";

const styles = theme => ({});

class ExportExamplesMold extends AbstractExportExamplesMold {

	constructor(props) {
		super(props);
		this.notifier = new ExportExamplesMoldNotifier(this);
		this.requester = new ExportExamplesMoldRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(ExportExamplesMold);