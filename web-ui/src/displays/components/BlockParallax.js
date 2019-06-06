import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractBlockParallax from "../../../gen/displays/components/AbstractBlockParallax";
import BlockParallaxNotifier from "../../../gen/displays/notifiers/BlockParallaxNotifier";
import BlockParallaxRequester from "../../../gen/displays/requesters/BlockParallaxRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class BlockParallax extends AbstractBlockParallax {

	constructor(props) {
		super(props);
		this.notifier = new BlockParallaxNotifier(this);
		this.requester = new BlockParallaxRequester(this);
	};

	refresh = (value) => {
	};
}

export default withStyles(styles, { withTheme: true })(BlockParallax);
DisplayFactory.register("BlockParallax", withStyles(styles, { withTheme: true })(BlockParallax));