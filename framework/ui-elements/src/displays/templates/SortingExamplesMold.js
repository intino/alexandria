import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractSortingExamplesMold from "../../../gen/displays/templates/AbstractSortingExamplesMold";
import SortingExamplesMoldNotifier from "../../../gen/displays/notifiers/SortingExamplesMoldNotifier";
import SortingExamplesMoldRequester from "../../../gen/displays/requesters/SortingExamplesMoldRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class SortingExamplesMold extends AbstractSortingExamplesMold {

	constructor(props) {
		super(props);
		this.notifier = new SortingExamplesMoldNotifier(this);
		this.requester = new SortingExamplesMoldRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(SortingExamplesMold);
DisplayFactory.register("SortingExamplesMold", withStyles(styles, { withTheme: true })(SortingExamplesMold));