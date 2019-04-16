import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractFileExamplesMold from "../../../gen/displays/templates/AbstractFileExamplesMold";
import FileExamplesMoldNotifier from "../../../gen/displays/notifiers/FileExamplesMoldNotifier";
import FileExamplesMoldRequester from "../../../gen/displays/requesters/FileExamplesMoldRequester";

const styles = theme => ({});

class FileExamplesMold extends AbstractFileExamplesMold {

	constructor(props) {
		super(props);
		this.notifier = new FileExamplesMoldNotifier(this);
		this.requester = new FileExamplesMoldRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(FileExamplesMold);