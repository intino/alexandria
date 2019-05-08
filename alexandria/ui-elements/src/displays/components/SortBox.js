import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractSortBox from "../../../gen/displays/components/AbstractSortBox";
import SortBoxNotifier from "../../../gen/displays/notifiers/SortBoxNotifier";
import SortBoxRequester from "../../../gen/displays/requesters/SortBoxRequester";

const styles = theme => ({});

class SortBox extends AbstractSortBox {

	constructor(props) {
		super(props);
		this.notifier = new SortBoxNotifier(this);
		this.requester = new SortBoxRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(SortBox);