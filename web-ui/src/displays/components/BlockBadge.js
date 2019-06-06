import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractBlockBadge from "../../../gen/displays/components/AbstractBlockBadge";
import BlockBadgeNotifier from "../../../gen/displays/notifiers/BlockBadgeNotifier";
import BlockBadgeRequester from "../../../gen/displays/requesters/BlockBadgeRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class BlockBadge extends AbstractBlockBadge {

	constructor(props) {
		super(props);
		this.notifier = new BlockBadgeNotifier(this);
		this.requester = new BlockBadgeRequester(this);
	};

	refresh = (value) => {
	};
}

export default withStyles(styles, { withTheme: true })(BlockBadge);
DisplayFactory.register("BlockBadge", withStyles(styles, { withTheme: true })(BlockBadge));