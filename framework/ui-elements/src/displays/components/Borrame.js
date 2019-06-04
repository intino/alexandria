import React from "react";
import { withStyles } from '@material-ui/core/styles';
import AbstractBorrame from "../../../gen/displays/components/AbstractBorrame";
import BorrameNotifier from "../../../gen/displays/notifiers/BorrameNotifier";
import BorrameRequester from "../../../gen/displays/requesters/BorrameRequester";
import DisplayFactory from 'alexandria-ui-elements/src/displays/DisplayFactory';

const styles = theme => ({});

class Borrame extends AbstractBorrame {

	constructor(props) {
		super(props);
		this.notifier = new BorrameNotifier(this);
		this.requester = new BorrameRequester(this);
	};


}

export default withStyles(styles, { withTheme: true })(Borrame);
DisplayFactory.register("Borrame", withStyles(styles, { withTheme: true })(Borrame));