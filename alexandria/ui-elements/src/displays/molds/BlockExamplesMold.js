import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractBlockExamplesMold from "../../../gen/displays/molds/AbstractBlockExamplesMold";
import BlockExamplesMoldNotifier from "../../../gen/displays/notifiers/BlockExamplesMoldNotifier";
import BlockExamplesMoldRequester from "../../../gen/displays/requesters/BlockExamplesMoldRequester";

const styles = theme => ({});

class BlockExamplesMold extends AbstractBlockExamplesMold {

	constructor(props) {
		super(props);
		this.notifier = new BlockExamplesMoldNotifier(this);
		this.requester = new BlockExamplesMoldRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(BlockExamplesMold);