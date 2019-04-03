import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractCollectionExamplesMold from "../../../gen/displays/molds/AbstractCollectionExamplesMold";
import CollectionExamplesMoldNotifier from "../../../gen/displays/notifiers/CollectionExamplesMoldNotifier";
import CollectionExamplesMoldRequester from "../../../gen/displays/requesters/CollectionExamplesMoldRequester";

const styles = theme => ({});

class CollectionExamplesMold extends AbstractCollectionExamplesMold {

	constructor(props) {
		super(props);
		this.notifier = new CollectionExamplesMoldNotifier(this);
		this.requester = new CollectionExamplesMoldRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(CollectionExamplesMold);