import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractMethodMold from "../../../gen/displays/templates/AbstractMethodMold";
import MethodMoldNotifier from "../../../gen/displays/notifiers/MethodMoldNotifier";
import MethodMoldRequester from "../../../gen/displays/requesters/MethodMoldRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class MethodMold extends AbstractMethodMold {

	constructor(props) {
		super(props);
		this.notifier = new MethodMoldNotifier(this);
		this.requester = new MethodMoldRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(MethodMold);
DisplayFactory.register("MethodMold", withStyles(styles, { withTheme: true })(MethodMold));