import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractFile from "../../../gen/displays/components/AbstractFile";
import FileNotifier from "../../../gen/displays/notifiers/FileNotifier";
import FileRequester from "../../../gen/displays/requesters/FileRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class File extends AbstractFile {

	constructor(props) {
		super(props);
		this.notifier = new FileNotifier(this);
		this.requester = new FileRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(File);
DisplayFactory.register("File", withStyles(styles, { withTheme: true })(File));