import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractSearchBox from "../../../gen/displays/components/AbstractSearchBox";
import SearchBoxNotifier from "../../../gen/displays/notifiers/SearchBoxNotifier";
import SearchBoxRequester from "../../../gen/displays/requesters/SearchBoxRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class SearchBox extends AbstractSearchBox {

	constructor(props) {
		super(props);
		this.notifier = new SearchBoxNotifier(this);
		this.requester = new SearchBoxRequester(this);
	};

	refreshCount = (value) => {
	};
}

export default withStyles(styles, { withTheme: true })(SearchBox);
DisplayFactory.register("SearchBox", withStyles(styles, { withTheme: true })(SearchBox));