import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractBlockExamplesMold from "../../../gen/displays/templates/AbstractBlockExamplesMold";
import BlockExamplesMoldNotifier from "../../../gen/displays/notifiers/BlockExamplesMoldNotifier";
import BlockExamplesMoldRequester from "../../../gen/displays/requesters/BlockExamplesMoldRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class BlockExamplesMold extends AbstractBlockExamplesMold {

	constructor(props) {
		super(props);
		this.notifier = new BlockExamplesMoldNotifier(this);
		this.requester = new BlockExamplesMoldRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(BlockExamplesMold);
DisplayFactory.register("BlockExamplesMold", withStyles(styles, { withTheme: true })(BlockExamplesMold));