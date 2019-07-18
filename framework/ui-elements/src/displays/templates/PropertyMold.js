import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractPropertyMold from "../../../gen/displays/templates/AbstractPropertyMold";
import PropertyMoldNotifier from "../../../gen/displays/notifiers/PropertyMoldNotifier";
import PropertyMoldRequester from "../../../gen/displays/requesters/PropertyMoldRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class PropertyMold extends AbstractPropertyMold {

	constructor(props) {
		super(props);
		this.notifier = new PropertyMoldNotifier(this);
		this.requester = new PropertyMoldRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(PropertyMold);
DisplayFactory.register("PropertyMold", withStyles(styles, { withTheme: true })(PropertyMold));