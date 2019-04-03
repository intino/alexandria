import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractCollectionList from "../../../gen/displays/components/AbstractCollectionList";
import CollectionListNotifier from "../../../gen/displays/notifiers/CollectionListNotifier";
import CollectionListRequester from "../../../gen/displays/requesters/CollectionListRequester";

const styles = theme => ({});

class CollectionList extends AbstractCollectionList {

	constructor(props) {
		super(props);
		this.notifier = new CollectionListNotifier(this);
		this.requester = new CollectionListRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(CollectionList);