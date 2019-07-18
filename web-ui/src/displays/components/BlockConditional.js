import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractBlockConditional from "../../../gen/displays/components/AbstractBlockConditional";
import BlockConditionalNotifier from "../../../gen/displays/notifiers/BlockConditionalNotifier";
import BlockConditionalRequester from "../../../gen/displays/requesters/BlockConditionalRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class BlockConditional extends AbstractBlockConditional {

	constructor(props) {
		super(props);
		this.notifier = new BlockConditionalNotifier(this);
		this.requester = new BlockConditionalRequester(this);
	};

	refreshVisibility = (value) => {
	};
}

export default withStyles(styles, { withTheme: true })(BlockConditional);
DisplayFactory.register("BlockConditional", withStyles(styles, { withTheme: true })(BlockConditional));