import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractList from "../../../gen/displays/components/AbstractList";
import ListNotifier from "../../../gen/displays/notifiers/ListNotifier";
import ListRequester from "../../../gen/displays/requesters/ListRequester";

const styles = theme => ({});

class List extends AbstractList {

	constructor(props) {
		super(props);
		this.notifier = new ListNotifier(this);
		this.requester = new ListRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(List);