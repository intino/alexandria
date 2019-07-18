import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractBaseGrouping from "../../../gen/displays/components/AbstractBaseGrouping";
import BaseGroupingNotifier from "../../../gen/displays/notifiers/BaseGroupingNotifier";
import BaseGroupingRequester from "../../../gen/displays/requesters/BaseGroupingRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class BaseGrouping extends AbstractBaseGrouping {

	constructor(props) {
		super(props);
		this.notifier = new BaseGroupingNotifier(this);
		this.requester = new BaseGroupingRequester(this);
	};

	refreshGroups = (value) => {
	};
}

export default withStyles(styles, { withTheme: true })(BaseGrouping);
DisplayFactory.register("BaseGrouping", withStyles(styles, { withTheme: true })(BaseGrouping));