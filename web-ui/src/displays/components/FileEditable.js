import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractFileEditable from "../../../gen/displays/components/AbstractFileEditable";
import FileEditableNotifier from "../../../gen/displays/notifiers/FileEditableNotifier";
import FileEditableRequester from "../../../gen/displays/requesters/FileEditableRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class FileEditable extends AbstractFileEditable {

	constructor(props) {
		super(props);
		this.notifier = new FileEditableNotifier(this);
		this.requester = new FileEditableRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(FileEditable);
DisplayFactory.register("FileEditable", withStyles(styles, { withTheme: true })(FileEditable));