import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractItem from "../../../gen/displays/components/AbstractItem";
import ItemNotifier from "../../../gen/displays/notifiers/ItemNotifier";
import ItemRequester from "../../../gen/displays/requesters/ItemRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class Item extends AbstractItem {

	constructor(props) {
		super(props);
		this.notifier = new ItemNotifier(this);
		this.requester = new ItemRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(Item);
DisplayFactory.register("Item", withStyles(styles, { withTheme: true })(Item));