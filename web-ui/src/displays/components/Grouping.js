import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractGrouping from "../../../gen/displays/components/AbstractGrouping";
import GroupingNotifier from "../../../gen/displays/notifiers/GroupingNotifier";
import GroupingRequester from "../../../gen/displays/requesters/GroupingRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class Grouping extends AbstractGrouping {

	constructor(props) {
		super(props);
		this.notifier = new GroupingNotifier(this);
		this.requester = new GroupingRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(Grouping);
DisplayFactory.register("Grouping", withStyles(styles, { withTheme: true })(Grouping));