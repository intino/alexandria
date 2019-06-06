import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractMethodParamMold from "../../../gen/displays/templates/AbstractMethodParamMold";
import MethodParamMoldNotifier from "../../../gen/displays/notifiers/MethodParamMoldNotifier";
import MethodParamMoldRequester from "../../../gen/displays/requesters/MethodParamMoldRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class MethodParamMold extends AbstractMethodParamMold {

	constructor(props) {
		super(props);
		this.notifier = new MethodParamMoldNotifier(this);
		this.requester = new MethodParamMoldRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(MethodParamMold);
DisplayFactory.register("MethodParamMold", withStyles(styles, { withTheme: true })(MethodParamMold));