import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractSelectorMenu from "../../../gen/displays/components/AbstractSelectorMenu";
import SelectorMenuNotifier from "../../../gen/displays/notifiers/SelectorMenuNotifier";
import SelectorMenuRequester from "../../../gen/displays/requesters/SelectorMenuRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class SelectorMenu extends AbstractSelectorMenu {

	constructor(props) {
		super(props);
		this.notifier = new SelectorMenuNotifier(this);
		this.requester = new SelectorMenuRequester(this);
	};

	refreshSelected = (value) => {
	};
}

export default withStyles(styles, { withTheme: true })(SelectorMenu);
DisplayFactory.register("SelectorMenu", withStyles(styles, { withTheme: true })(SelectorMenu));