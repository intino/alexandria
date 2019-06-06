import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractCollection from "../../../gen/displays/components/AbstractCollection";
import CollectionNotifier from "../../../gen/displays/notifiers/CollectionNotifier";
import CollectionRequester from "../../../gen/displays/requesters/CollectionRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class Collection extends AbstractCollection {

	constructor(props) {
		super(props);
		this.notifier = new CollectionNotifier(this);
		this.requester = new CollectionRequester(this);
	};

	setup = (value) => {
	};

	refresh = () => {
	};

	refreshItemCount = (value) => {
	};
}

export default withStyles(styles, { withTheme: true })(Collection);
DisplayFactory.register("Collection", withStyles(styles, { withTheme: true })(Collection));