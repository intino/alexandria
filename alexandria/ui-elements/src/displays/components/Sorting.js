import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractSorting from "../../../gen/displays/components/AbstractSorting";
import SortingNotifier from "../../../gen/displays/notifiers/SortingNotifier";
import SortingRequester from "../../../gen/displays/requesters/SortingRequester";

const styles = theme => ({});

class Sorting extends AbstractSorting {

	constructor(props) {
		super(props);
		this.notifier = new SortingNotifier(this);
		this.requester = new SortingRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(Sorting);