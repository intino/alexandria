import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractBaseFile from "../../../gen/displays/components/AbstractBaseFile";
import BaseFileNotifier from "../../../gen/displays/notifiers/BaseFileNotifier";
import BaseFileRequester from "../../../gen/displays/requesters/BaseFileRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class BaseFile extends AbstractBaseFile {

	constructor(props) {
		super(props);
		this.notifier = new BaseFileNotifier(this);
		this.requester = new BaseFileRequester(this);
	};

	refresh = (value) => {
	};
}

export default withStyles(styles, { withTheme: true })(BaseFile);
DisplayFactory.register("BaseFile", withStyles(styles, { withTheme: true })(BaseFile));