import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractBlock from "../../../gen/displays/components/AbstractBlock";
import BlockNotifier from "../../../gen/displays/notifiers/BlockNotifier";
import BlockRequester from "../../../gen/displays/requesters/BlockRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class Block extends AbstractBlock {

	constructor(props) {
		super(props);
		this.notifier = new BlockNotifier(this);
		this.requester = new BlockRequester(this);
	};

	refreshSpacing = (value) => {
	};

	refreshLayout = (value) => {
	};
}

export default withStyles(styles, { withTheme: true })(Block);
DisplayFactory.register("Block", withStyles(styles, { withTheme: true })(Block));