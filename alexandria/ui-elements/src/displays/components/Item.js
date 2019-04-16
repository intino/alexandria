import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractItem from "../../../gen/displays/components/AbstractItem";
import ItemNotifier from "../../../gen/displays/notifiers/ItemNotifier";
import ItemRequester from "../../../gen/displays/requesters/ItemRequester";

const styles = theme => ({});

class Item extends AbstractItem {

	constructor(props) {
		super(props);
		this.notifier = new ItemNotifier(this);
		this.requester = new ItemRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(Item);