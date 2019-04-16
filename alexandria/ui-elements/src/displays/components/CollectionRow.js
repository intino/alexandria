import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractCollectionRow from "../../../gen/displays/components/AbstractCollectionRow";
import CollectionRowNotifier from "../../../gen/displays/notifiers/CollectionRowNotifier";
import CollectionRowRequester from "../../../gen/displays/requesters/CollectionRowRequester";

const styles = theme => ({});

class CollectionRow extends AbstractCollectionRow {

	constructor(props) {
		super(props);
		this.notifier = new CollectionRowNotifier(this);
		this.requester = new CollectionRowRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(CollectionRow);