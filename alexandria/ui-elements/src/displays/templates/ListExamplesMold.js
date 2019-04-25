import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractListExamplesMold from "../../../gen/displays/templates/AbstractListExamplesMold";
import ListExamplesMoldNotifier from "../../../gen/displays/notifiers/ListExamplesMoldNotifier";
import ListExamplesMoldRequester from "../../../gen/displays/requesters/ListExamplesMoldRequester";

const styles = theme => ({});

class ListExamplesMold extends AbstractListExamplesMold {

	constructor(props) {
		super(props);
		this.notifier = new ListExamplesMoldNotifier(this);
		this.requester = new ListExamplesMoldRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(ListExamplesMold);