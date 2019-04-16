import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractItemMold from "../../../gen/displays/templates/AbstractItemMold";
import ItemMoldNotifier from "../../../gen/displays/notifiers/ItemMoldNotifier";
import ItemMoldRequester from "../../../gen/displays/requesters/ItemMoldRequester";

const styles = theme => ({});

class ItemMold extends AbstractItemMold {

	constructor(props) {
		super(props);
		this.notifier = new ItemMoldNotifier(this);
		this.requester = new ItemMoldRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(ItemMold);