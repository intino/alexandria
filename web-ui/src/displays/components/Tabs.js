import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractTabs from "../../../gen/displays/components/AbstractTabs";
import TabsNotifier from "../../../gen/displays/notifiers/TabsNotifier";
import TabsRequester from "../../../gen/displays/requesters/TabsRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class Tabs extends AbstractTabs {

	constructor(props) {
		super(props);
		this.notifier = new TabsNotifier(this);
		this.requester = new TabsRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(Tabs);
DisplayFactory.register("Tabs", withStyles(styles, { withTheme: true })(Tabs));