import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractSearchBoxExamplesMold from "../../../gen/displays/templates/AbstractSearchBoxExamplesMold";
import SearchBoxExamplesMoldNotifier from "../../../gen/displays/notifiers/SearchBoxExamplesMoldNotifier";
import SearchBoxExamplesMoldRequester from "../../../gen/displays/requesters/SearchBoxExamplesMoldRequester";

const styles = theme => ({});

class SearchBoxExamplesMold extends AbstractSearchBoxExamplesMold {

	constructor(props) {
		super(props);
		this.notifier = new SearchBoxExamplesMoldNotifier(this);
		this.requester = new SearchBoxExamplesMoldRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(SearchBoxExamplesMold);