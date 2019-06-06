import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractGroupingExamplesMold from "../../../gen/displays/templates/AbstractGroupingExamplesMold";
import GroupingExamplesMoldNotifier from "../../../gen/displays/notifiers/GroupingExamplesMoldNotifier";
import GroupingExamplesMoldRequester from "../../../gen/displays/requesters/GroupingExamplesMoldRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class GroupingExamplesMold extends AbstractGroupingExamplesMold {

	constructor(props) {
		super(props);
		this.notifier = new GroupingExamplesMoldNotifier(this);
		this.requester = new GroupingExamplesMoldRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(GroupingExamplesMold);
DisplayFactory.register("GroupingExamplesMold", withStyles(styles, { withTheme: true })(GroupingExamplesMold));