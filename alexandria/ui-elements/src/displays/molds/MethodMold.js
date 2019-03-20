import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractMethodMold from "../../../gen/displays/molds/AbstractMethodMold";
import MethodMoldNotifier from "../../../gen/displays/notifiers/MethodMoldNotifier";
import MethodMoldRequester from "../../../gen/displays/requesters/MethodMoldRequester";

const styles = theme => ({});

class MethodMold extends AbstractMethodMold {

	constructor(props) {
		super(props);
		this.notifier = new MethodMoldNotifier(this);
		this.requester = new MethodMoldRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(MethodMold);